# Claude 활용 기록

회사 도입을 제안할 때 근거로 쓸 기록. 작업마다 한 줄씩 남긴다.

| 날짜 | 작업 | 사용한 Claude 기능 | 잘 된 점 | Claude가 틀린 점 / 내가 막힌 점 |
|---|---|---|---|---|
| 2026-10-03 | 환경 세팅 (JDK, Spring Initializr, Git, GitHub) | Cowork 채팅 + 내 PC 폴더 연결 | Claude가 폴더를 직접 읽어서 실행 흔적을 근거로 점검함 (테스트 미실행, 커밋 누락, 패키지명 불일치 발견) | Initializr에서 Java 27 선택, `note/` 하위 폴더 생성, 폴더 이동이 파일 잠금으로 절반만 됨 |
| 2026-10-03 | ① Note 엔티티 + 도메인 테스트 | 채팅으로 코드 받고 직접 타이핑 | 테스트 결과 XML과 로그를 Claude가 읽어서 "통과했지만 `@Entity` 누락"을 찾아냄 | 테스트 파일을 `src/main`에 생성, static import 누락, `isequalTo` 오타, `@Entity` 누락 (테스트로는 안 잡힘) |
