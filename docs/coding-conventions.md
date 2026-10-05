# 공통 코드 컨벤션

`domain`·`application`·`adapter` 코드에 적용한다.

- 클래스는 `final`로 선언한다. 명시적인 상속 계층의 기반 클래스(예: `abstract sealed` 기반 예외)는 제외하며, `application`·`adapter`에서는 상속 기반 프록시가 필요한 클래스도 제외한다. 어댑터의 JPA Entity도 제외한다. enum·record는 언어가 제공하는 선언 방식을 따른다.
- 메서드·생성자의 모든 파라미터는 `final`로 선언한다.
- 서비스·어댑터의 의존성은 생성자 주입과 `private final` 필드를 사용한다.
- 분기·이름·책임 분리 등 객체지향 코드 스타일은 [생활 체조](object-calisthenics.md) 규칙을 따른다.

도메인 객체의 상태 불변성과 생성 규칙은 [도메인](domain-conventions.md), 예외의 상속 계층과 생성자 규칙은 [예외](exception-conventions.md) 컨벤션을 따른다.
