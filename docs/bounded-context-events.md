# BC 간 이벤트 통합

최상위 `catalog`, `cart`, `order`, `member`, `review`를 BC로 취급한다.
`catalog/product`와 `catalog/category`는 같은 Catalog BC의 내부 영역이다.
같은 BC 안에서는 기존 도메인 ID와 포트를 사용할 수 있다.

다른 BC의 도메인 객체, ID 타입, UseCase, 저장소를 직접 참조하지 않는다.
외부 ID는 소비하는 BC가 소유하는 `{Domain}ReferenceId`로 변환한다.
현재 Member/Product 참조는 Cart, Order, Review에 있고, Cart 참조는 Order에,
Order 참조는 Catalog의 재고 예약에 있다. 모든 ID는 기존 UUID v7 규칙을 따른다.
웹 요청과 Integration Event 계약에서는 문자열을 사용하고 Application에서 로컬 VO로 변환한다.

## 패키지

```text
{bc}/
├── domain/event/                         # 내부 Domain Event
├── application/
│   ├── {Bc}EventMapper.java              # 내부 이벤트 → 공개 계약
│   └── port/
│       ├── in/                           # 수신 처리 UseCase, Command
│       └── out/
│           ├── IntegrationEventPort.java
│           └── event/                    # 공개 Integration Event
└── adapter/
    ├── in/messaging/                     # 공개 계약 수신 → Command → UseCase
    ├── out/messaging/                    # Outbox 기록, Broker 전송
    └── out/persistence/
        ├── outbox/
        ├── inbox/
        └── projection/                   # 소비 BC의 로컬 조회 모델
```

Catalog의 상품 Domain Event는 기존 영역에 맞춰 `catalog/product/domain/event`에,
재고 예약 Domain Event와 BC 간 계약은 `catalog/domain/event` 및
`catalog/application/port/out/event`에 둔다.
Member와 Review는 현재 발행만 하므로 Outbox만 구현한다.
Inbox와 수신 UseCase는 실제 소비자인 Catalog, Cart, Order에 둔다.

## 공개 계약과 흐름

| 발행 BC | 공개 이벤트 | 소비 BC와 처리 |
| --- | --- | --- |
| Catalog | ProductChanged | Cart의 상품 이름·가격 조회 모델 갱신 |
| Cart | CartChanged | Order의 장바구니 조회 모델 갱신 |
| Order | OrderPlaced | Catalog가 전체 주문 항목의 재고 예약 |
| Catalog | StockReserved / StockRejected | Order의 주문 확정 / 거절 |
| Order | OrderConfirmed | Cart가 요청 당시 버전의 장바구니 비우기 |
| Order | OrderCancelled | Catalog가 해당 주문의 예약 재고 해제 |
| Catalog | StockReleased | 예약 해제 공개 사실 |
| Order | OrderRejected | 주문 거절 공개 사실 |
| Member | MemberRegistered | 회원 등록 공개 사실 |
| Review | ReviewWritten / ReviewDeleted | 리뷰 작성·삭제 공개 사실 |

각 공개 이벤트는 BC별 sealed Integration Event 타입을 구현한다.
계약은 문자열 ID, 원시 값, 불변 목록으로 구성하고 Domain VO와 기술 타입을 노출하지 않는다.
이벤트 타입은 `catalog.product-changed.v1`처럼 이름과 버전을 고정한다.
직렬화된 Java 클래스 이름을 전송하거나 동적으로 역직렬화하지 않는다.

Envelope에는 eventId, source, eventType, aggregateId, sequence, occurredAt, payload가 있다.
sequence는 발행 BC Outbox의 저장 순서이며 BC마다 별도 범위를 가진다.
상품 조회 모델은 sequence, 장바구니 조회 모델은 sequence와 도메인 revision으로
이전 스냅샷의 덮어쓰기를 막는다.

## 주문 API와 일관성

`POST /api/orders`는 HTTP 202와 `STOCK_PENDING`을 반환한다.
재고 예약 성공 후 `PENDING`, 실패 후 `REJECTED`가 된다.
클라이언트는 기존 주문 상세 조회 API에서 결과를 확인한다.
상품 또는 장바구니 조회 모델이 아직 없으면 HTTP 409와
`PRODUCT_SNAPSHOT_NOT_READY` 또는 `CART_SNAPSHOT_NOT_READY`를 반환한다.
빈 장바구니와 동일 장바구니 버전에 대한 중복 주문도 HTTP 409로 처리한다.

Order는 Cart나 Catalog를 조회하거나 변경하는 UseCase를 호출하지 않는다.
주문 요청은 로컬 장바구니 스냅샷을 잠그고 사용한 revision을 기록한다.
Catalog는 상품 ID 순으로 잠금을 획득하고 전체 항목을 먼저 검증한다.
재고 부족이나 상품 누락 시 일부 상품만 차감되지 않는다.
예약 결과 실패 시 사용한 장바구니 revision을 다시 주문할 수 있다.

취소가 예약 결과보다 먼저 도착하면 Order를 확정하거나 Cart를 비우지 않는다.
Catalog는 예약이 존재하는 경우에만 재고를 복구하며,
예약 전에 도착한 취소는 RELEASED 상태로 남겨 이후 예약을 막는다.
다른 eventId로 같은 주문이 다시 전달되어도 예약 상태로 중복 차감·복구를 방지한다.

주문 요청 이후 Cart가 수정되었다면 늦게 도착한 OrderConfirmed로 해당 Cart를 비우지 않는다.
수정한 내용은 유지되며 이후 주문은 갱신된 CartChanged 스냅샷을 사용한다.

## 저장과 전달

도메인 변경과 Outbox 기록은 같은 트랜잭션에 참여한다.
Outbox 기록은 활성 트랜잭션을 요구한다.
스케줄러는 BC별 미발행 행을 순서대로 잠그고 최대 100건을 전송한다.
전송 성공 시 publishedAt을 기록하고, 실패 시 attempts·lastError·nextAttemptAt을 남긴다.
재시도 간격은 1초부터 최대 60초까지 늘어나며 실패한 선행 이벤트가 처리되기 전까지
해당 BC의 후속 이벤트를 보내지 않는다.

수신자의 Inbox, 로컬 변경, 후속 Outbox는 발신자와 분리된 하나의 트랜잭션으로 커밋된다.
수신 처리 실패 시 모두 롤백한다.
수신자 커밋 후 발신자의 완료 기록이 실패해도 같은 eventId를 재전송하면 Inbox가 중복 처리를 막는다.
Inbox의 기본 키는 eventId이고, 재고 예약의 기본 키는 로컬 OrderReferenceId다.

현재 `LocalMessageBroker`는 Spring 이벤트를 이용해 단일 애플리케이션 안에서 전송한다.
Outbox 스케줄러가 요청 트랜잭션이 커밋된 후 전송하며 수신 결과가 반환된 뒤 발행 완료를 기록한다.
외부 브로커를 연결할 때는 `MessageBroker` 전송 구현과 수신 어댑터를 교체하고
메시지 ID 유지, 영속 전송 확인, 수신 트랜잭션 커밋 후 ACK 원칙을 유지한다.

## 시작과 설정

시작 시 Catalog와 Cart는 자신이 소유한 현재 스냅샷을 100건씩 Outbox에 기록한다.
이미 저장된 상품·장바구니 데이터도 이벤트로 조회 모델을 채울 수 있으며,
`PublishProductSnapshotsUseCase`와 `PublishCartSnapshotsUseCase`로 재발행할 수 있다.
스냅샷을 발행하기 전에 최신 상태를 잠그고 다시 읽는다.

기본 설정은 다음과 같다.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: ${JPA_DDL_AUTO:update}
messaging:
  outbox:
    enabled: true
    poll-interval-ms: 250
  snapshots:
    publish-on-startup: true
```

기본 스키마 설정은 애플리케이션 종료 시 Outbox/Inbox를 삭제하지 않는다.
영속 DB를 사용해야 재시작 후 이벤트를 보존할 수 있다.
테스트는 별도 H2 DB와 create-drop 설정으로 격리한다.
기존 영속 스키마를 사용하는 환경에서는 추가된 Cart revision, Order의 Cart 참조·revision,
재고 예약 및 이벤트 테이블을 데이터에 맞게 마이그레이션해야 한다.
기존 동기 처리로 차감된 주문은 재고를 다시 차감하지 않고 예약 내역을 백필해야 한다.

## 검증

`./gradlew test --offline --no-daemon`으로 전체 검증을 실행한다.

아키텍처 테스트는 BC 간 참조가 수신 어댑터의 공개 이벤트 계약에만 존재하는지 확인한다.
DB 통합 테스트는 기본·동시성 안전 모드에서 정상 주문, 실패, 중복,
재시도, Inbox/Outbox 롤백, 취소 경합, 오래된 스냅샷, 동시 주문 및 조회 모델 복구를 검증한다.
파일 H2 DB를 사용하는 재시작 테스트는 실제 LocalMessageBroker로
미발행 이벤트 보존과 재전송을 확인한다.
