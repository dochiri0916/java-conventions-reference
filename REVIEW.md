# 예외·에러 처리 아키텍처 리뷰

## 1. 결론

현재 구현은 **계층 분리의 방향은 적절하지만, 예외·에러 처리 전체가 Clean Architecture, DDD, Hexagonal Architecture와 객체 지향 원칙을 충분히 만족한다고 보기는 어렵다.**

Domain/Application 예외가 Spring Web과 분리되어 있고, HTTP 변환을 Web Adapter가 담당하며, RFC 9457 `ProblemDetail` 생성도 중앙화되어 있다는 점은 좋다. 반면 실제 오류 계약에는 다음과 같은 운영상 중요한 구멍이 있다.

- 실제로 발생하는 회원 입력 오류 두 종류가 매핑에서 빠져 400이 아니라 500으로 처리된다.
- `member`, `global` 외 컨텍스트에는 API 메시지 카탈로그가 없어 구체적인 오류도 모두 일반 문구로 응답된다.
- DB 제약 조건 및 동시성 오류가 Port 계약으로 번역되지 않아 기술 예외 또는 `IllegalStateException`이 Application까지 새어 나온다.
- 알 수 없는 500 오류를 응답에서 숨기기는 하지만 서버 로그에도 남기지 않는다.
- Spring MVC 기본 오류와 비즈니스 오류의 `ProblemDetail` 형태가 통일되어 있지 않다.
- Domain/Application에 사용자 노출용 한국어 문자열과 자유 형식 `detail`이 남아 있다.
- 다른 Bounded Context의 예외를 직접 던지고 전역 Web 매퍼에 의존하여 컨텍스트별 오류 계약의 소유권이 흐려져 있다.

따라서 판정은 **부분 충족 — 구조적 기반은 양호하나 오류 계약의 완전성, Port 실패 격리, Bounded Context 독립성은 개선 필요**이다.

| 평가 항목 | 판정 | 요약 |
|---|---|---|
| Clean Architecture | 부분 충족 | Domain/Application이 HTTP를 모르지만, 기술 실패와 타 컨텍스트 예외가 내부 경계를 넘는다. |
| DDD | 부분 충족 | 전용 Domain 예외와 ErrorCode는 좋지만, 표현 문자열·자유 형식 detail·타 컨텍스트 모델 결합이 남아 있다. |
| Hexagonal Architecture | 개선 필요 | 저장소 Port의 정상 반환 계약은 있으나 실패 계약이 안정적이지 않다. |
| 객체 지향/OCP/SRP | 부분 충족 | 정적 팩토리와 역할 분리는 좋지만 매핑·메시지·타입 분기가 중복되어 실제 누락이 발생했다. |
| HTTP 오류 계약 | 개선 필요 | 일부 오류는 좋은 `ProblemDetail`을 만들지만 코드·메시지·기본 MVC 오류의 완전성이 부족하다. |

## 2. 리뷰 범위와 검증

- 기준 커밋: `2694526` (`main`, `origin/main`)
- 리뷰 일자: 2026-07-10
- 범위:
  - `global.error` 전체
  - 각 컨텍스트의 `domain.exception`, `application.exception`
  - 예외 발생 지점인 Domain 모델, Command, Application Service
  - `ErrorCodeMappingProvider`, `ApiErrorMessageProvider`
  - Persistence/Event Adapter의 실패 변환
  - 관련 단위·통합·Web 테스트
- 실행 결과:
  - `./gradlew check` — 성공
  - `./gradlew test --rerun-tasks` — 성공
  - 69 test suites, 477 tests, 0 failures, 0 errors, 0 skipped

`check`는 Checkstyle, PMD, SpotBugs, JaCoCo, ArchUnit 및 프로젝트 컨벤션 검사를 모두 통과했다. 다만 아래 문제들은 정적 계층 규칙보다 **런타임 오류 코드의 완전성 및 실패 의미 번역**에 관한 문제이므로 현재 검증에 잡히지 않는다.

## 3. 현재 오류 처리 흐름

```text
Domain/Application
  RuntimeException + context ErrorCode
              |
              v
context.adapter.in.web
  ErrorCodeMappingProvider
  - exception type -> error code
  - error code -> HTTP status + ProblemType
              |
              v
global.error
  GlobalExceptionHandler
    -> ApiErrorMessageCatalog
    -> ApiProblemDetailFactory
    -> RFC 9457 ProblemDetail
```

의존성 방향 자체는 안쪽에서 바깥쪽으로 흐른다. 문제는 각 단계가 별도 목록으로 유지되어, 예외 추가 시 아래 세 곳을 사람이 동시에 맞춰야 한다는 점이다.

1. Domain/Application 예외와 ErrorCode
2. 예외 타입 및 HTTP 매핑
3. 사용자 메시지 카탈로그

실제로 이 목록 사이에 드리프트가 발생해 500 오응답과 일반 메시지 대체가 생겼다.

## 4. 잘된 점

### 4.1 Domain/Application이 HTTP 기술을 모른다

Domain/Application 예외에서 `HttpStatus`, `ProblemDetail`, `ErrorResponse`, JPA/DB 예외를 import하지 않는다. HTTP 상태와 Problem Type은 각 컨텍스트의 Web Adapter에서 결정한다. 이는 의존성 역전과 표현 계층 분리 원칙에 부합한다.

근거:

- `src/main/java/com/dochiri/tooltestlab/member/domain/exception/InvalidEmailException.java`
- `src/main/java/com/dochiri/tooltestlab/member/application/exception/DuplicateEmailException.java`
- `src/main/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorCodeMappingProvider.java`

### 4.2 도메인 불변식이 전용 예외로 표현된다

Value Object와 Aggregate는 null, blank, 범위, 상태 전이를 전용 unchecked exception으로 표현한다. 예외 생성자는 private이고 `required()`, `invalidRating(...)`, `lowStock(...)` 같은 정적 팩토리를 제공하며 ErrorCode를 보존한다.

근거:

- `src/main/java/com/dochiri/tooltestlab/review/domain/model/Rating.java:7`
- `src/main/java/com/dochiri/tooltestlab/product/domain/model/Stock.java:7`
- `src/main/java/com/dochiri/tooltestlab/order/domain/model/Order.java:75`

### 4.3 전역 핸들러가 특정 컨텍스트 예외를 직접 알지 않는다

`GlobalExceptionHandler`는 Domain/Application 예외를 직접 import하거나 switch하지 않고 `ApiExceptionMapper`와 provider 목록을 조합한다. 컨텍스트별 매핑 책임이 Web Adapter에 있으므로 신규 컨텍스트 추가 시 전역 핸들러를 수정하지 않아도 되는 구조다.

근거:

- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalExceptionHandler.java:29`
- `src/main/java/com/dochiri/tooltestlab/global/error/ApiExceptionMapper.java:15`
- `src/main/java/com/dochiri/tooltestlab/global/error/ErrorCodeMappingProvider.java:8`

### 4.4 오류 응답 생성과 사용자 메시지 노출이 분리되어 있다

`ApiProblemDetailFactory`는 status, type, title, detail, 안정 코드, timestamp를 일관되게 조립한다. `GlobalExceptionHandler`는 예외의 `getMessage()`를 API detail로 노출하지 않으므로 내부 정보 노출 위험도 낮다. `member` 컨텍스트는 별도 메시지 provider를 두어 목표 구조를 잘 보여준다.

근거:

- `src/main/java/com/dochiri/tooltestlab/global/error/ApiProblemDetailFactory.java:20`
- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalExceptionHandler.java:80`
- `src/main/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorMessageProvider.java:13`

### 4.5 인증 오류가 Web Adapter 책임으로 처리된다

인증 인터셉터와 argument resolver는 인증 정보가 없을 때 `null`을 반환하지 않고 `AuthenticationRequiredException`을 던진다. 이 예외는 `GLOBAL.AUTHENTICATION_REQUIRED`, 401, 한국어 메시지로 변환된다.

근거:

- `src/main/java/com/dochiri/tooltestlab/global/web/ApiAuthenticationInterceptor.java:28`
- `src/main/java/com/dochiri/tooltestlab/global/web/AuthenticatedMemberArgumentResolver.java:28`
- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalErrorCodeMappingProvider.java:12`
- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalErrorMessageProvider.java:10`

## 5. 주요 발견 사항

### HIGH-1. 실제로 발생하는 회원 입력 ErrorCode 두 개가 매핑되지 않아 500이 된다

`InvalidCommandException`은 이메일과 비밀번호 누락 시 각각 `MemberErrorCode.EMAIL_REQUIRED`, `MemberErrorCode.PASSWORD_REQUIRED`를 사용한다.

- `src/main/java/com/dochiri/tooltestlab/member/application/exception/InvalidCommandException.java:16`
- `src/main/java/com/dochiri/tooltestlab/member/application/port/in/LoginMemberCommand.java:6`
- `src/main/java/com/dochiri/tooltestlab/member/application/port/in/RegisterMemberCommand.java:6`

그러나 `MemberErrorCodeMappingProvider`에는 Application ErrorCode 중 `NAME_REQUIRED`, `MEMBER_NOT_FOUND`, `LOGIN_FAILED`, `DUPLICATE_EMAIL`, `INVALID_INPUT`만 있고 `EMAIL_REQUIRED`, `PASSWORD_REQUIRED`는 없다.

- `src/main/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorCodeMappingProvider.java:28`
- `src/main/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorMessageProvider.java:15`

따라서 흐름은 다음과 같다.

```text
blank/null email or password
  -> InvalidCommandException(EMAIL_REQUIRED or PASSWORD_REQUIRED)
  -> MemberErrorCodeMappingProvider.resolve(...) returns empty
  -> ApiExceptionMapper returns empty
  -> GlobalExceptionHandler.handleUnexpected()
  -> 500 GLOBAL.INTERNAL_SERVER_ERROR
```

`RegisterMemberRequest`와 `LoginMemberRequest`에도 Bean Validation 제약이 없으므로 이 경로는 실제 Web 요청으로 도달 가능하다.

테스트는 `"회원 에러 코드 15개를 모두 매핑한다"`고 주장하지만 단순히 Map 크기만 15인지 검사한다. 실제 enum은 Domain 10개와 Application 7개로 총 17개이고, `InvalidCommandException` resolve 테스트도 `nameRequired()`만 확인한다.

- `src/test/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorCodeMappingProviderTest.java:28`
- `src/test/java/com/dochiri/tooltestlab/member/adapter/in/web/MemberErrorCodeMappingProviderTest.java:267`

권고:

1. 두 ErrorCode에 400/`VALIDATION_FAILED` 매핑과 한국어 title/detail을 추가한다.
2. 회원 가입·로그인의 null/blank email/password Web 테스트에서 status뿐 아니라 `type`, `code`, `title`, `detail`을 검증한다.
3. Map 크기 대신 **실제 발생 가능한 코드 집합과 등록된 매핑·메시지 집합의 동등성**을 검증한다.

### HIGH-2. 다섯 컨텍스트의 구체적인 오류 메시지가 모두 일반 문구로 대체된다

`ApiErrorMessageCatalog.findByCode(...)`는 등록된 메시지가 없으면 항상 아래 일반 문구를 반환한다.

```text
title: 요청 처리 실패
detail: 요청을 처리할 수 없습니다.
```

근거: `src/main/java/com/dochiri/tooltestlab/global/error/ApiErrorMessageCatalog.java:20`, `:40`

현재 `ApiErrorMessageProvider` 구현은 다음 두 개뿐이다.

- `GlobalErrorMessageProvider`
- `MemberErrorMessageProvider`

반면 cart, category, order, product, review에는 HTTP status/ProblemType provider만 있고 메시지 provider가 없다. 예를 들어 `ProductNotFoundException`은 404와 `PRODUCT.PRODUCT_NOT_FOUND` 코드까지는 유지되지만 title/detail은 상품 없음이 아니라 일반 실패 문구가 된다. 같은 문제가 장바구니 없음, 빈 장바구니, 재고 부족, 리뷰 중복, 소유권 위반 등 전체에 적용된다.

또한 해당 컨텍스트의 Domain/Application ErrorCode enum이 한국어 문자열과 `title()`을 가지고 있지만, Web 메시지 카탈로그는 이를 전혀 사용하지 않는다. 즉 표현 문자열이 안쪽 계층에 있으면서도 실제 응답에는 반영되지 않는 이중 실패다.

권고:

1. 단기적으로 cart/category/order/product/review에 context-local `ApiErrorMessageProvider`를 추가한다.
2. 중기적으로 status/type와 title/detail을 한 `ApiErrorDescriptor` 레지스트리에서 관리하여 두 목록의 드리프트를 막는다.
3. 애플리케이션 시작 또는 테스트 시 `모든 노출 매핑 코드에 정확히 하나의 메시지가 존재함`을 검증한다.

### HIGH-3. Persistence Adapter의 실패 타입이 Port 계약으로 번역되지 않는다

정상 경로는 Repository Port로 잘 추상화되어 있지만 실패 경로는 안정적이지 않다.

#### 회원

`members.email`은 unique이지만 `MemberPersistenceAdapter.save(...)`는 `DataIntegrityViolationException`을 변환하지 않는다. Service의 `existsByEmail(...)` 선검사는 순차 중복은 막지만 동시 요청의 check-then-act race는 막지 못한다.

- `src/main/java/com/dochiri/tooltestlab/member/adapter/out/persistence/MemberEntity.java:27`
- `src/main/java/com/dochiri/tooltestlab/member/application/service/RegisterMemberService.java:25`
- `src/main/java/com/dochiri/tooltestlab/member/adapter/out/persistence/MemberPersistenceAdapter.java:17`

동시 중복은 `DuplicateEmailException`/409가 아니라 기술 예외/500이 된다.

#### 카테고리

`categories.name`은 unique이지만 중복 카테고리 Application 예외와 변환이 전혀 없다.

- `src/main/java/com/dochiri/tooltestlab/category/adapter/out/persistence/CategoryEntity.java:31`
- `src/main/java/com/dochiri/tooltestlab/product/adapter/out/persistence/ProductPersistenceAdapter.java:75`

동일 이름을 재등록하면 DB 제약 실패가 그대로 500으로 분류된다.

#### 리뷰

일반 `ReviewPersistenceAdapter`는 unique 제약 실패를 그대로 노출한다. safe 구현은 이를 잡지만 `DuplicateReviewException`이 아니라 사용자 문구를 가진 `IllegalStateException`으로 바꾼다. 둘 다 `ReviewRepositoryPort`의 같은 구현체인데 설정에 따라 실패 타입이 달라져 LSP와 Port 계약 안정성을 해친다. `IllegalStateException`은 매핑되지 않으므로 역시 500이다.

- `src/main/java/com/dochiri/tooltestlab/review/adapter/out/persistence/ReviewEntity.java:16`
- `src/main/java/com/dochiri/tooltestlab/review/adapter/out/persistence/ReviewPersistenceAdapter.java:20`
- `src/main/java/com/dochiri/tooltestlab/review/adapter/out/persistence/SafeReviewPersistenceAdapter.java:26`

safe-mode 테스트도 전용 예외/ErrorCode가 아니라 넓은 `RuntimeException`과 Adapter 문자열을 기대하여 잘못된 계약을 고정한다.

- `src/test/java/com/dochiri/tooltestlab/config/SafeModeTest.java:247`

권고:

1. DB constraint name/SQL state를 Adapter 내부에서 식별하고 해당 Application 예외로 변환한다.
2. member는 `DuplicateEmailException`, review는 `DuplicateReviewException`, category는 새 `DuplicateCategoryNameException`처럼 UseCase 의미를 보존한다.
3. `save()` 지연 flush로 catch 범위 밖에서 실패하지 않도록 Adapter의 flush 경계를 명확히 하고 통합 테스트한다.
4. normal/safe Adapter에 동일한 Port 실패 계약 테스트를 적용한다.
5. 외부 Port가 기술 예외를 노출하지 않는다는 아키텍처 테스트를 추가한다.

### MEDIUM-1. 예상하지 못한 500 오류가 서버 로그에도 남지 않는다

`GlobalExceptionHandler.handleRuntime(...)`은 매핑되지 않은 예외를 `handleUnexpected()`로 보내지만 원본 예외를 전달하지 않는다. `handleUnexpected()`에도 logger가 없다.

- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalExceptionHandler.java:33`
- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalExceptionHandler.java:40`

클라이언트에 원본 메시지나 stack trace를 숨기는 것은 맞지만, 서버에서도 원인을 잃으면 장애 분석이 불가능하다.

권고:

- `handleUnexpected(RuntimeException exception)`에서 stack trace를 error level로 기록한다.
- 응답에는 일반 메시지만 유지한다.
- MDC의 trace/request ID를 `ProblemDetail` extension과 로그에 함께 기록한다.
- 매핑 누락과 실제 서버 장애를 로그에서 구분할 수 있도록 unmapped exception class를 포함한다.

### MEDIUM-2. Spring MVC 기본 오류는 동일한 ProblemDetail 계약을 거치지 않는다

`GlobalExceptionHandler`는 `ResponseEntityExceptionHandler`를 확장하지만 직접 표준화한 항목은 `MethodArgumentNotValidException`뿐이다.

- `src/main/java/com/dochiri/tooltestlab/global/error/GlobalExceptionHandler.java:52`

잘못된 JSON, query parameter 타입 불일치, 필수 parameter 누락, 지원하지 않는 HTTP method, 정적 resource/handler 부재 등은 상위 클래스의 기본 처리로 빠진다. 이 응답들은 `ApiProblemDetailFactory`를 거치지 않으므로 context code, timestamp, 프로젝트의 type URI, 한국어 catalog가 비즈니스 오류와 동일하게 보장되지 않는다.

권고:

- 관련 Spring MVC 예외를 `GLOBAL.*` 안정 코드로 정의한다.
- `handleExceptionInternal` 또는 각 표준 handler를 통해 모든 MVC 오류를 `ApiProblemDetailFactory`로 정규화한다.
- malformed JSON, type mismatch, missing parameter, unsupported method, not-found route 테스트에서 동일한 응답 필드를 검증한다.

### MEDIUM-3. Domain/Application 예외에 표현 문자열과 자유 형식 detail이 섞여 있다

member를 제외한 다수 ErrorCode enum은 한국어 사용자 메시지를 보관하고 `title()`을 노출한다.

- `src/main/java/com/dochiri/tooltestlab/cart/domain/exception/CartDomainErrorCode.java:5`
- `src/main/java/com/dochiri/tooltestlab/order/domain/exception/OrderDomainErrorCode.java:5`
- `src/main/java/com/dochiri/tooltestlab/product/domain/exception/ProductDomainErrorCode.java:5`
- `src/main/java/com/dochiri/tooltestlab/review/application/exception/ReviewApplicationErrorCode.java:5`

Command와 Domain 모델도 예외에 사용자 문장 형태의 자유 문자열을 전달한다.

- `src/main/java/com/dochiri/tooltestlab/cart/application/port/in/AddToCartCommand.java:7`
- `src/main/java/com/dochiri/tooltestlab/review/application/port/in/WriteReviewCommand.java:6`
- `src/main/java/com/dochiri/tooltestlab/product/domain/model/Stock.java:7`
- `src/main/java/com/dochiri/tooltestlab/order/domain/model/ZipCode.java:7`

그러나 현재 Web Adapter는 이 `detail`을 사용하지 않는다. 그 결과 Domain/Application이 표현 책임을 가지면서도 API에는 일반 메시지만 나가는 중복과 불일치가 생긴다.

예외 내부 필드도 `detailField`, `actualSizeField`, `expectedField`, `currentStockField`처럼 기계적인 이름을 사용한다. 식별자는 `String`으로 보관하는 경우가 많아 `missingOrderId`, `duplicatedEmail`이라는 맥락은 있어도 타입 안정성은 잃는다.

권고:

- ErrorCode는 사용자 문구 없이 도메인 의미만 가진 enum으로 만든다.
- `invalidInput(String detail)` 하나 대신 `emailRequired()`, `quantityNotPositive(int value)`처럼 규칙별 코드와 팩토리를 둔다.
- 예외는 필요한 VO나 상태만 보존하고 사용자 title/detail은 Web Adapter catalog에서만 결정한다.
- `detailField` 대신 `invalidQuantity`, `requestedQuantity`, `missingOrderId`처럼 실패 의미가 드러나는 필드를 사용한다.

### MEDIUM-4. Bounded Context가 다른 컨텍스트의 예외 계약에 직접 의존한다

cart와 order Application Service는 product의 Outbound Port 및 Application 예외를 직접 사용한다. cart/review/order Domain 모델도 member, product, order의 VO와 예외를 교차 import한다.

- `src/main/java/com/dochiri/tooltestlab/cart/application/service/AddToCartService.java:7`
- `src/main/java/com/dochiri/tooltestlab/order/application/service/PlaceOrderService.java:16`
- `src/main/java/com/dochiri/tooltestlab/cart/domain/model/Cart.java:5`
- `src/main/java/com/dochiri/tooltestlab/review/domain/model/Review.java:3`

예를 들어 cart의 상품 조회 실패가 cart 소유 예외가 아니라 `ProductNotFoundException`이 되고, cart API 응답은 product Web Adapter의 provider가 Spring Bean으로 존재해야만 정상 매핑된다. 컨텍스트를 모듈이나 서비스로 분리하면 이 암묵적 결합이 즉시 깨진다.

권고:

- 소비 컨텍스트가 자신의 Outbound Port와 실패 의미를 소유한다. 예: `LoadProductForCartPort`, `CartProductNotFoundException`.
- 다른 컨텍스트 Adapter는 해당 Port를 구현하거나 Application 간 명시적 facade를 둔다.
- 여러 컨텍스트에서 정말 같은 VO를 공유한다면 별도 shared-kernel 모듈과 명시적 안정성 정책을 둔다.
- 타 컨텍스트 예외를 그대로 전파하지 말고 소비 UseCase의 언어로 번역한다.

### MEDIUM-5. 매핑 구조가 OCP를 지향하지만 실제로는 세 목록의 수동 동기화에 의존한다

각 context provider는 다음을 반복한다.

- ErrorCode별 status/type Map entry
- 예외 타입별 `instanceof` 체인
- 별도 메시지 Map entry

예: `src/main/java/com/dochiri/tooltestlab/review/adapter/in/web/ReviewErrorCodeMappingProvider.java:27`, `:45`

새 오류 하나를 추가할 때 provider의 Map, resolve 분기, message provider, 테스트를 모두 수정해야 한다. 전역 핸들러는 OCP에 가깝지만 context provider 내부는 수정 지점이 많고, HIGH-1과 HIGH-2가 그 드리프트의 실제 사례다.

권고:

- Web Adapter에서 한 오류의 `code/status/type/title/detail`을 하나의 immutable descriptor로 등록한다.
- exception-to-code 추출은 descriptor 또는 context-local registry와 같은 위치에서 관리한다.
- 중복 코드, 중복 exception resolver, 매핑 없는 메시지, 메시지 없는 매핑을 애플리케이션 시작 시 fail-fast 검증한다.
- 공통 `CodedException` 계약이 필요하다면 Domain/Application이 `global.error`에 의존하지 않도록 별도 dependency-free shared 모듈에만 둔다.

### MEDIUM-6. 커밋 이후 후속 처리 실패에 대한 오류 정책이 없다

주문 이벤트는 `TransactionSynchronization.afterCommit()`에서 동기 발행되고, listener는 다시 `AFTER_COMMIT`으로 선언되어 여러 UseCase를 순차 호출한다.

- `src/main/java/com/dochiri/tooltestlab/order/adapter/out/event/SpringOrderEventAdapter.java:28`
- `src/main/java/com/dochiri/tooltestlab/order/adapter/in/event/OrderPlacedEventListener.java:18`
- `src/main/java/com/dochiri/tooltestlab/order/application/service/HandleOrderPlacedService.java:32`

후속 재고 차감이나 장바구니 비우기에서 예외가 발생하면 원 주문 트랜잭션은 이미 커밋된 상태다. 현재 listener에는 재시도, 보상, 실패 저장, dead-letter, idempotency 계약이 없다. 따라서 예외가 호출자에게 전파되는지와 무관하게 주문/재고/장바구니 사이에 불일치가 남을 수 있다.

권고:

- 주문 이벤트를 Outbox에 영속화하고 독립 consumer에서 재시도·멱등 처리한다.
- 최소한 listener별 실패를 격리하고 orderId, handler 이름, 원인을 구조화 로그와 실패 저장소에 남긴다.
- 한 handler 실패가 다른 handler 실행을 막을지 정책을 명시하고 테스트한다.
- `afterCommit` 수동 발행과 `@TransactionalEventListener(AFTER_COMMIT)`을 중첩하지 말고 하나의 커밋 이후 메커니즘으로 단순화한다.

### LOW-1. 사용되지 않는 오류 코드와 손실되는 예외 문맥이 있다

다음 코드는 매핑되어 있지만 Production 발생 지점이 없거나 실제 예외가 다른 컨텍스트 코드를 사용한다.

- `CartErrorCode.CART_ITEM_NOT_FOUND`
- `CartErrorCode.NOT_CART_OWNER`
- `OrderDomainErrorCode.PRODUCT_ID_REQUIRED`
- `OrderDomainErrorCode.PRODUCT_NAME_REQUIRED`
- `OrderDomainErrorCode.UNIT_PRICE_REQUIRED`
- `MemberErrorCode.INVALID_INPUT`

반대로 `InvalidMaxCountException.notPositive(int value)`, `InvalidMoneyException.negative(long amount)` 등은 인자를 받지만 예외에 보존하지 않는다.

죽은 코드는 “등록 개수” 테스트를 통과시키면서 실제 완전성을 흐리고, 버려진 문맥은 로그와 정책 판단에 활용할 수 없다.

권고:

- 발생 지점 없는 코드는 제거하거나 필요한 전용 예외와 함께 구현한다.
- 예외 문맥이 필요하면 의미 있는 VO/상태 필드로 보존하고, 필요하지 않으면 팩토리 인자도 제거한다.
- 축약형 `INVALID_CAT_ID`, `INVALID_MBR_NAME`, `BAD_RVW_CONTENT`보다 ubiquitous language가 드러나는 전체 이름을 사용한다.

## 6. 권장 목표 구조

```text
[Domain/Application]
  - semantic exception
  - semantic error code
  - typed failure context only
  - no HTTP, DB, SDK, user-facing message

            ^ adapter translates

[Outbound Adapter]
  - DB/SDK exception
  - identify exact technical cause
  - convert to Port failure or Application exception

            v

[Context Web Adapter]
  ApiErrorDescriptor
  - stable API code
  - HTTP status
  - ProblemType
  - Korean title/detail
  - exception/code resolver

            v

[Global Web Adapter]
  - compose context descriptors
  - validate duplicates/completeness at startup
  - normalize MVC errors
  - log unmapped exceptions with trace ID
  - create one RFC 9457 response shape
```

핵심은 예외 계층을 무조건 상속 구조로 통합하는 것이 아니라, **실패 의미의 소유권과 각 경계의 번역 책임을 명확히 하는 것**이다.

## 7. 우선순위별 개선 순서

### 1단계 — 즉시 수정

1. `MemberErrorCode.EMAIL_REQUIRED`, `PASSWORD_REQUIRED`의 HTTP 매핑과 메시지를 추가한다.
2. cart/category/order/product/review의 메시지 provider를 추가한다.
3. 예상하지 못한 RuntimeException을 stack trace와 trace ID로 기록한다.
4. 회원, 카테고리, 리뷰 unique 제약 실패를 Application 예외/409로 번역한다.

### 2단계 — 계약 강화

1. 모든 ErrorCode에 대해 mapping과 message가 정확히 하나씩 존재하는 completeness test를 추가한다.
2. normal/safe Persistence Adapter에 동일한 실패 계약 테스트를 적용한다.
3. 모든 Spring MVC 기본 오류를 같은 `ProblemDetail` 형식으로 정규화한다.
4. API 실패 테스트에서 status만 보지 말고 type/code/title/detail/timestamp를 검증한다.

### 3단계 — 경계 리팩터링

1. 타 컨텍스트 Port/예외 직접 참조를 소비 컨텍스트 소유 Port와 예외로 교체한다.
2. Domain/Application의 사용자 문자열과 자유 형식 detail을 제거한다.
3. status/type/message를 단일 Web descriptor로 통합한다.
4. 주문 후속 처리를 Outbox + 재시도 + 멱등성 구조로 전환한다.

## 8. 필수 추가 테스트

| 계층 | 테스트 | 핵심 검증 |
|---|---|---|
| Web | 회원 email/password null·blank | 400, 정확한 `MEMBER.*` code, 한국어 title/detail |
| Web | 각 컨텍스트 대표 Domain/Application 예외 | status, type, code, title, detail, timestamp |
| Web | malformed JSON/type mismatch/missing parameter/unsupported method | 동일한 RFC 9457 구조와 `GLOBAL.*` code |
| Global error | registry completeness | mapping code 집합과 message code 집합의 일치, 중복 없음 |
| Persistence | 회원 동시 중복 가입 | `DuplicateEmailException`, 409 계약 |
| Persistence | 동일 카테고리명 저장 | 전용 Application 예외 및 409 계약 |
| Persistence | 리뷰 중복 — normal/safe | 둘 다 `DuplicateReviewException`, 동일 ErrorCode |
| Application | Port 기술 실패 | DB/SDK 타입이 Service 밖으로 새지 않음 |
| Event | after-commit handler 실패 | 재시도, 실패 저장, 멱등성, 다른 handler 실행 정책 |
| Observability | unmapped RuntimeException | 응답은 일반 500, 서버 로그에는 원인과 trace ID 보존 |

## 9. 최종 판정

이 프로젝트는 예외를 Domain/Application에 두고 HTTP 변환을 Adapter로 밀어낸다는 큰 방향, 전용 ErrorCode와 정적 팩토리, 전역 handler/provider 조합이라는 기반은 잘 잡혀 있다.

하지만 현재 상태에서는 다음 세 가지 때문에 운영 가능한 오류 처리 아키텍처로 완료되었다고 보기 어렵다.

1. 실제 발생 코드와 Web 매핑 사이에 누락이 있어 4xx가 500으로 바뀐다.
2. Outbound Adapter 실패가 Port 의미로 번역되지 않아 기술 세부사항이 안쪽으로 샌다.
3. Bounded Context 및 메시지 소유권이 분명하지 않아 다른 컴포넌트의 존재와 수동 목록 동기화에 의존한다.

우선 HIGH-1부터 HIGH-3까지 해결하고 completeness test를 추가하면, 현재 구조를 크게 뒤엎지 않고도 Clean/Hexagonal 원칙과 API 오류 계약의 신뢰도를 빠르게 높일 수 있다.
