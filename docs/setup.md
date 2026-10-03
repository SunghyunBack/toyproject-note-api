# 개발 환경 설치 (Windows + VS Code)

## 1. JDK 21
1. Eclipse Temurin 21(adoptium.net)을 설치한다. 설치 중 "Set JAVA_HOME" 옵션을 켠다.
2. 새 터미널에서 확인한다.
   ```
   java -version
   ```
   `21.x`가 나오면 성공.

## 2. VS Code 확장
- **Extension Pack for Java** (Microsoft): 언어 지원, 디버거, 테스트 러너
- **Spring Boot Extension Pack**: Spring Initializr, Spring Boot Dashboard
- **Gradle for Java**: Gradle 태스크 실행
- **Claude Code**: Claude를 에디터 안에서 사용

## 3. 프로젝트 생성
1. `Ctrl+Shift+P` → `Spring Initializr: Create a Gradle Project`
2. 선택값
   - Spring Boot: 목록의 최신 안정 버전 (SNAPSHOT, M, RC가 붙은 버전은 제외)
   - Language: Java / Java version: 21
   - Group: `com.sunghyeon` / Artifact: `note`
   - Packaging: Jar
   - Dependencies: Spring Web, Spring Data JPA, H2 Database, Validation, Lombok(선택)
3. 생성 위치로 이 폴더(`note program`)를 선택한다.

## 4. 실행 확인
```
.\gradlew.bat test
.\gradlew.bat bootRun
```
브라우저에서 `http://localhost:8080`에 접속했을 때 Whitelabel Error Page가 나오면 정상이야. 아직 API가 없다는 뜻이거든.

## 5. Git
```
git init
git add .
git commit -m "init: Spring Boot 프로젝트 생성"
```
