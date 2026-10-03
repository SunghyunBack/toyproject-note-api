# 개인 학습 노트 앱

## 목적
학습 노트를 관리하는 REST API. 동시에 개발자(나)가 Spring을 학습하고, Claude 사용법을 익혀 회사 업무로 가져가기 위한 토이 프로젝트.
요구사항과 MVP 범위는 `docs/requirements.md`를 따른다. 이 범위를 벗어나는 기능은 먼저 제안만 하고 구현하지 않는다.

## 환경
- OS: Windows, 에디터: VS Code
- Java 25 (LTS), Spring Boot 4.1.x, Gradle 9.x (Groovy DSL)
- Spring Boot 4는 스타터 이름이 3.x와 다르다 (예: `spring-boot-starter-webmvc`). 3.x 예제를 그대로 가져오지 않는다.
- Spring Web, Spring Data JPA, Validation, H2 (추후 PostgreSQL)
- 베이스 패키지: `com.sunghyun.note`
- `javax.*`가 아니라 `jakarta.*` 패키지를 사용한다.
- 테스트 실행: `.\gradlew.bat test` (Windows)

## 구조 규칙
- 레이어별 패키지 구조를 쓴다: `controller` / `service` / `repository` / `domain` / `dto`. (학습 목적상 레이어 개념을 눈에 보이게 하려고 의도적으로 선택함)
- controller는 요청/응답 변환만 한다. 비즈니스 로직은 service와 domain에만 둔다.
- 도메인 로직(검증, 상태 변경)은 Spring 없이 `new`로 객체를 만들어 테스트할 수 있게 작성한다.
- 엔티티를 API 응답으로 직접 노출하지 않고 DTO를 쓴다.
- 의존성은 생성자 주입(Constructor Injection)만 사용한다. 필드 주입(`@Autowired` 필드)은 금지.
- 예외는 `@RestControllerAdvice`에서 일괄 처리하고, 에러 응답 형식을 통일한다.
- 요청 DTO 검증은 Bean Validation(`@Valid`)으로 한다.

## 작업 규칙
- 한 번에 하나의 기능만 변경한다.
- 기능 추가 시 테스트를 함께 작성한다. 도메인과 서비스는 단위 테스트(Unit Test), API는 `@WebMvcTest`로 작성한다.
- 작업이 끝나면 `.\gradlew.bat test`를 실행하고 결과를 보여준다.
- 커밋 전에 다음을 제시한다.
  1. 변경된 파일별 핵심 변경점 요약 (파일당 3줄 이내)
  2. 내가 답해야 할 확인 질문 1개
  내가 그 질문에 답하기 전에는 커밋하지 않는다.
- 작업이 끝나면 `docs/usage-log.md`에 한 줄 기록을 추가한다. 형식은 그 파일을 따른다.

## 학습 규칙 (중요)
- 새로운 어노테이션이나 Spring 기능을 처음 쓸 때는 그게 내부적으로 하는 일을 짧게 설명한다.
- 설계 선택지가 여러 개면 선택 이유와 트레이드오프(trade-off)를 말한다.
- 설치나 환경 설정이 필요하면 `docs/setup.md`를 기준으로 단계별로 안내한다.
- 새 개념(어노테이션, 설정, 도구, 용어)이 처음 나오면 `docs/study-notes.md`의 맞는 섹션에 추가한다. 이미 있는 항목은 중복으로 쓰지 않고 보완한다. 형식은 "무엇 → 왜 필요 → 이 프로젝트의 어디"를 따른다.
- 설명은 한국어로 하고, 전문용어는 한국어와 영어를 병기한다.
