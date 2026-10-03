# 학습 정리 노트

이 프로젝트를 진행하면서 나온 개념을 정리한 노트. 대화에서 새 개념이 나올 때마다 추가한다.
각 항목은 **무엇인지 → 왜 필요한지 → 이 프로젝트에서 어디에 있는지** 순서로 적는다.

---

## 0. 전체 그림: 누가 무슨 일을 하나

```
[JDK]          자바 코드를 컴파일하고 실행하는 도구 모음
   ↑ 사용
[Gradle]       빌드 도구: 라이브러리 다운로드 → 컴파일 → 테스트 → 패키징 자동화
   ↑ 설정 파일
[build.gradle] "이 프로젝트는 어떤 라이브러리를 쓰고, 어떻게 빌드한다"는 명세
   ↓ 다운로드
[Spring Boot + 스타터들]  실제 앱이 사용하는 프레임워크와 라이브러리
```

### .NET(C#)과 대응표 (Revit 개발 경험과 연결)

| Java / Spring | .NET / C# | 역할 |
|---|---|---|
| JDK | .NET SDK | 컴파일러 + 런타임 |
| Gradle (`gradlew`) | `dotnet` CLI / MSBuild | 빌드 도구 |
| `build.gradle` | `.csproj` | 프로젝트 설정, 의존성 목록 |
| `dependencies { implementation ... }` | `<PackageReference>` | 외부 라이브러리 참조 |
| Maven Central | nuget.org | 라이브러리 저장소 |
| `.jar` | `.dll` | 컴파일된 결과물 묶음 |
| Spring 컨테이너의 DI | `Microsoft.Extensions.DependencyInjection` | 의존성 주입(Dependency Injection) |

---

## 0.5 기술 스택 분류: 지금 쓰는 것들을 어디에 놓아야 하나

### 0.5.1 분류 기준 세 가지
기술은 하나의 기준으로만 나누면 헷갈려. 아래 세 가지 질문을 따로따로 던지면 정리돼.

**① 어디서 실행되나? (실행 위치)**
| 구분 | 의미 | 예 |
|---|---|---|
| 프론트엔드(Frontend) | 사용자 브라우저/앱에서 실행. 화면을 그리고 입력을 받음 | HTML, CSS, JavaScript, React |
| 백엔드(Backend) | 서버에서 실행. 요청을 받아 로직을 처리하고 데이터를 저장 | Java + Spring Boot |
| 데이터베이스(Database) | 데이터를 영구 저장하고 조회 | H2, PostgreSQL |
| 인프라(Infra) | 위의 것들을 띄우고 운영하는 환경 | Docker, 서버, IIS |

**② 어떤 층(Layer)에 속하나? (아래로 갈수록 기반)**
| 층 | 의미 | 예 |
|---|---|---|
| 언어(Language) | 코드를 쓰는 문법 | Java |
| 런타임(Runtime) | 그 언어로 만든 프로그램을 실제로 실행하는 환경 | JVM (JDK에 포함) |
| 프레임워크(Framework) | 앱의 뼈대와 실행 흐름을 정해주는 큰 틀 | Spring Boot |
| 라이브러리(Library) | 특정 기능만 제공하는 부품 | Hibernate, Jackson |

**③ 언제 쓰이나? (개발 시점 vs 실행 시점)**
- 실행 시점(Runtime)에 앱 안에 포함돼서 같이 돌아가는 것: Spring Boot, Hibernate, H2 등
- 개발 시점(Development time)에만 쓰고, 최종 앱에는 포함되지 않는 도구: Gradle, VS Code, Git, JUnit, Claude

### 0.5.2 프레임워크와 라이브러리의 차이
- **라이브러리**: **내 코드가** 라이브러리를 호출해. 예를 들어 내가 `objectMapper.writeValueAsString(note)`처럼 직접 불러서 쓰는 식이야.
- **프레임워크**: **프레임워크가** 내 코드를 호출해. 나는 `@RestController` 클래스를 만들어두기만 하고, 언제 실행할지는 Spring이 HTTP 요청이 들어올 때 정해.
- 이렇게 제어 흐름의 주도권이 넘어가는 걸 **제어의 역전(IoC, Inversion of Control)**이라고 해. Spring 컨테이너를 "IoC 컨테이너"라고 부르는 이유야.
- Revit으로 비유하면: 네가 만든 `IExternalCommand.Execute()`는 네가 직접 호출하는 게 아니라 Revit이 호출하지? 그 구조에서 Revit API는 프레임워크 역할을 하는 거야.

### 0.5.3 이 프로젝트의 기술 지도 (현재)

| 실행 위치 | 층 | 기술 | 이 프로젝트에서 하는 일 |
|---|---|---|---|
| **백엔드** | 언어 | Java 25 | 모든 서버 코드 |
| | 런타임 | JVM (Temurin JDK 25) | Java 코드 실행 |
| | 프레임워크 | Spring Boot 4.1 | 앱 뼈대, 자동 설정, DI 컨테이너 |
| | 프레임워크 (Spring 하위) | Spring MVC | HTTP 요청을 Controller로 연결 |
| | 프레임워크 (Spring 하위) | Spring Data JPA | Repository 인터페이스 자동 구현 |
| | 라이브러리 | Hibernate | JPA 구현체. 객체 ↔ SQL 변환 |
| | 라이브러리 | Hibernate Validator | `@NotBlank` 같은 입력 검증 |
| | 라이브러리 | Jackson | Java 객체 ↔ JSON 변환 |
| | 라이브러리 | HikariCP | DB 커넥션 풀 |
| | 웹 서버 | 내장 Tomcat | HTTP 요청 수신 |
| **데이터베이스** | DB | H2 (메모리 모드) | 노트 저장. 나중에 PostgreSQL로 교체 |
| **프론트엔드** | - | **아직 없음** (MVP 범위 밖) | 지금은 브라우저 주소창, curl, HTTP 클라이언트로 API를 직접 호출해서 확인 |
| **통신 규약** | - | HTTP + REST + JSON | 프론트와 백엔드가 주고받는 약속 |
| **개발 도구** | 빌드 | Gradle 9.7.1 (설정 언어: Groovy) | 의존성 다운로드, 컴파일, 테스트 |
| | 테스트 | JUnit 5, AssertJ, Mockito | 테스트 실행, 검증, 가짜 객체(Mock) |
| | 에디터 | VS Code | 코드 작성 |
| | 버전 관리 | Git + GitHub | 변경 기록, 원격 백업 |
| | AI | Claude | 학습, 코드 가이드 |
| **설정/문서** | 형식 | `.properties`, Markdown | 앱 설정, 문서 |

### 0.5.4 요청 하나가 흘러가는 길
```
[클라이언트: 브라우저/curl]           ← 프론트엔드 자리 (지금은 비어 있음)
      │  HTTP 요청 (JSON)
      ▼
[내장 Tomcat]                         ← 웹 서버
      ▼
[Spring MVC → NoteController]         ← 백엔드: 표현 계층 (Presentation)
      ▼
[NoteService]                         ← 백엔드: 비즈니스 계층 (Business)
      ▼
[NoteRepository (Spring Data JPA)]    ← 백엔드: 영속 계층 (Persistence)
      ▼
[Hibernate → JDBC]                    ← 라이브러리: SQL 생성, 실행
      ▼
[H2]                                  ← 데이터베이스
```
나중에 프론트엔드를 만들면 맨 위 클라이언트 자리에 들어가. 백엔드는 바꿀 필요 없이 같은 JSON API를 그대로 쓰면 돼. 프론트와 백엔드를 이렇게 HTTP로 분리해두면 서로 독립적으로 바꿀 수 있어.

---

## 1. JDK (Java Development Kit)

- **무엇**: 자바 컴파일러(`javac`), 실행기(`java`), 표준 라이브러리를 묶은 개발 도구 모음
- **JRE와 차이**: JRE(Java Runtime Environment)는 실행만 할 수 있어. 개발하려면 컴파일러가 들어 있는 JDK가 필요해.
- **LTS(Long-Term Support)**: 장기 지원 버전. 17, 21, 25가 LTS야. 그 사이 버전(22, 23, 26, 27 등)은 6개월만 지원돼. 학습이나 실무에는 LTS를 쓰는 게 기본이야.
- **Temurin**: 자바 버전 이름이 아니라, Eclipse 재단이 빌드해서 무료로 배포하는 JDK 배포판(Distribution)이야. Oracle JDK, Amazon Corretto 같은 다른 배포판도 있어.

---

## 2. Gradle: 빌드 도구(Build Tool)

### 2.1 빌드 도구가 왜 필요한가
빌드 도구가 없으면 이 작업을 직접 해야 해.
1. 라이브러리 `.jar` 파일 수십 개를 직접 다운로드
2. 그 라이브러리가 의존하는 라이브러리(전이 의존성, Transitive Dependency)까지 찾아서 다운로드
3. `javac`에 클래스패스(Classpath)를 길게 넘겨서 컴파일
4. 테스트 실행, `.jar`로 패키징

Gradle은 `build.gradle`만 읽고 이 과정을 전부 자동으로 처리해. Maven이라는 다른 빌드 도구도 있는데, 설정을 XML(`pom.xml`)로 써.

### 2.2 Gradle Wrapper: `gradlew`, `gradlew.bat`
- **무엇**: Gradle을 실행하는 작은 스크립트. `gradlew`는 Linux/Mac용, `gradlew.bat`은 Windows용이야.
- **왜 필요**: PC마다 설치된 Gradle 버전이 다르면 같은 프로젝트가 어디서는 빌드되고 어디서는 실패해. Wrapper는 **프로젝트가 정한 버전의 Gradle을 자동으로 다운로드해서** 실행해. 그래서 Gradle을 따로 설치할 필요가 없고, 팀원 모두가 같은 버전으로 빌드하게 돼.
- **관련 파일**
  - `gradle/wrapper/gradle-wrapper.properties`: 사용할 Gradle 버전을 정해. 이 프로젝트는 `gradle-9.7.1`이야.
  - `gradle/wrapper/gradle-wrapper.jar`: 실제로 Gradle을 다운로드하는 프로그램. `.gitignore`에 `!gradle/wrapper/gradle-wrapper.jar` 예외가 있는 이유는, 이 jar는 반드시 Git에 올려야 하기 때문이야. 없으면 다른 PC에서 wrapper가 동작하지 않아.
- **다운로드 위치**: 처음 실행할 때 `C:\Users\<이름>\.gradle\wrapper\dists`에 받아져. 그래서 첫 실행만 느려.

### 2.3 `settings.gradle`
- `rootProject.name = 'note'`: 프로젝트 이름이야. 여러 모듈로 나뉜 프로젝트(멀티 모듈, Multi-module)일 때는 여기에 모듈 목록도 적어.

### 2.4 `build.gradle` 한 줄씩

```groovy
plugins {
    id 'java'                                          // ①
    id 'org.springframework.boot' version '4.1.1'      // ②
    id 'io.spring.dependency-management' version '1.1.7' // ③
}
```
- **플러그인(Plugin)**: Gradle에 기능(태스크, Task)을 추가해.
- ① `java`: 컴파일(`compileJava`), 테스트(`test`), jar 생성(`jar`) 태스크를 추가해.
- ② Spring Boot 플러그인: `bootRun`(앱 실행)과 `bootJar` 태스크를 추가해. `bootJar`는 라이브러리를 전부 포함해서 `java -jar`로 바로 실행되는 jar(실행 가능한 jar, Executable/Fat JAR)를 만들어.
- ③ 의존성 관리 플러그인: 아래 `dependencies`에 **버전이 없는 이유**가 이거야. Spring Boot가 "서로 호환되는 라이브러리 버전 조합표"(BOM, Bill of Materials)를 제공하고, 이 플러그인이 그 표에서 버전을 자동으로 채워. 라이브러리끼리 버전이 충돌하는 문제를 막아줘.

```groovy
group = 'com.sunghyun'
version = '0.0.1-SNAPSHOT'
```
- **group / artifact / version**: 라이브러리를 식별하는 좌표(Coordinates)야. artifact는 `settings.gradle`의 이름(`note`)이야.
- **SNAPSHOT**: 아직 개발 중이라 내용이 바뀔 수 있는 버전이라는 표시야.

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(27)
    }
}
```
- **툴체인(Toolchain)**: "이 프로젝트는 몇 버전 JDK로 컴파일한다"를 고정하는 설정이야. Gradle을 실행한 JDK와는 상관없이, PC에서 지정한 버전의 JDK를 찾아서 써.
- 지정한 버전의 JDK가 PC에 없고, 자동 다운로드 설정(foojay 플러그인)도 없으면 빌드가 실패해.
- 이 프로젝트는 설치된 JDK 25(LTS)에 맞춰 `25`로 설정했어. Initializr 생성 직후에는 `27`이었어.

```groovy
repositories {
    mavenCentral()
}
```
- **저장소(Repository)**: 라이브러리를 어디서 다운로드할지 정해. Maven Central은 자바 생태계의 공용 저장소야(.NET의 nuget.org 역할).

```groovy
dependencies {
    implementation     '...'   // 컴파일 + 실행에 필요
    runtimeOnly        '...'   // 실행할 때만 필요
    testImplementation '...'   // 테스트 코드에서만 필요
    testRuntimeOnly    '...'   // 테스트 실행할 때만 필요
}
```
- **의존성 구성(Dependency Configuration)**: 라이브러리가 **언제** 필요한지 구분해.
  - `implementation`: 내 코드가 그 라이브러리의 클래스를 직접 `import`해서 써.
  - `runtimeOnly`: 내 코드는 직접 쓰지 않고, 실행할 때만 있으면 돼. 예를 들어 H2는 JPA가 내부적으로 쓰고 내 코드는 H2 클래스를 import하지 않아. 이렇게 나눠두면 실수로 H2에 직접 의존하는 코드를 쓸 수 없어서, 나중에 PostgreSQL로 바꾸기 쉬워져.
  - `test*`: 테스트 코드에만 쓰이고, 최종 jar에는 포함되지 않아.

```groovy
tasks.named('test') {
    useJUnitPlatform()
}
```
- `test` 태스크가 JUnit 5(JUnit Platform)로 테스트를 실행하도록 설정해.

---

## 3. Spring Boot

### 3.1 Spring과 Spring Boot의 차이
- **Spring Framework**: DI 컨테이너, 웹 MVC, 트랜잭션 같은 기능을 제공하는 프레임워크. 기능은 강력하지만 설정할 게 많아.
- **Spring Boot**: Spring을 **설정 없이 바로 실행되게** 만들어주는 도구. 핵심 기능은 두 가지야.
  1. **스타터(Starter)**: 기능별로 필요한 라이브러리를 묶어둔 의존성 세트
  2. **자동 설정(Auto-configuration)**: 클래스패스에 어떤 라이브러리가 있는지 보고 필요한 객체(Bean)를 자동으로 만들어. 예를 들어 H2가 클래스패스에 있고 DB 주소 설정이 없으면, 메모리 DB를 자동으로 띄우고 연결해.
- 이 "자동"이 편하지만, **동작은 하는데 왜 되는지 모르는** 상황이 생기는 주원인이기도 해. 막히면 `application.properties`에 `debug=true`를 넣어봐. 실행 로그에 어떤 자동 설정이 적용됐고 어떤 건 왜 빠졌는지 보고서(Condition Evaluation Report)가 출력돼.

### 3.2 Spring Boot 4의 특징
- 이 프로젝트는 Spring Boot **4.1.1**을 써. 4.x부터는 자동 설정이 기능별 모듈로 잘게 나뉘었어. 그래서 3.x 때 `spring-boot-starter-web` 하나이던 게 `spring-boot-starter-webmvc`로 이름이 바뀌었고, 테스트 스타터도 기능별(`*-test`)로 나뉘었어.
- 인터넷 예제는 대부분 3.x 기준이라서, 의존성 이름이 다르게 나올 수 있다는 걸 기억해둬.
- `javax.*`가 아니라 `jakarta.*` 패키지를 써(3.x부터 적용).

### 3.3 이 프로젝트의 의존성

| 의존성 | 구성 | 무엇 | 이 프로젝트에서 쓰는 곳 |
|---|---|---|---|
| `spring-boot-starter-webmvc` | implementation | Spring MVC + 내장 톰캣(Embedded Tomcat) + JSON 변환(Jackson). 톰캣이 내장돼 있어서 서버를 따로 설치하지 않아도 돼. | `@RestController`, `/api/notes` |
| `spring-boot-starter-data-jpa` | implementation | JPA + Hibernate + Spring Data JPA + 커넥션 풀(HikariCP) | `@Entity`, `NoteRepository` |
| `spring-boot-starter-validation` | implementation | Bean Validation의 구현체(Hibernate Validator) | `@NotBlank`, `@Size`, `@Valid` |
| `com.h2database:h2` | runtimeOnly | 자바로 만든 경량 DB. 메모리 모드에서는 앱을 끄면 데이터가 사라져. | 개발용 DB |
| `spring-boot-h2console` | implementation | 브라우저에서 H2 DB 내용을 볼 수 있는 웹 콘솔(`/h2-console`) | 저장된 데이터 확인용 |
| `*-test` 스타터 3개 | testImplementation | JUnit 5, AssertJ, Mockito, 그리고 `@WebMvcTest`, `@DataJpaTest` 같은 테스트 슬라이스(Test Slice) | 테스트 코드 |
| `junit-platform-launcher` | testRuntimeOnly | Gradle이 JUnit 5 테스트를 실행할 때 쓰는 런처 | `.\gradlew.bat test` |

### 3.4 JPA 관련 용어 (헷갈리기 쉬움)
- **ORM(Object-Relational Mapping)**: 자바 객체와 DB 테이블을 자동으로 매핑하는 기술. SQL을 직접 쓰지 않아도 객체를 저장하고 조회할 수 있어.
- **JPA(Jakarta Persistence API)**: ORM의 **표준 명세(인터페이스)**야. 실제 코드가 아니라 규칙이야.
- **Hibernate**: JPA 명세를 실제로 구현한 **구현체**야.
- **Spring Data JPA**: JPA 위에 한 층 더 얹은 도구. `interface NoteRepository extends JpaRepository<Note, Long>`처럼 **인터페이스만 선언하면** 구현 클래스를 Spring이 실행 시점에 자동으로 만들어줘.
- 관계: `내 코드 → Spring Data JPA → JPA(규칙) → Hibernate(구현) → JDBC → H2`

---

## 4. 프로젝트 폴더와 파일

| 경로 | 무엇 | Git에 올리나 |
|---|---|---|
| `src/main/java` | 앱 소스 코드 | O |
| `src/main/resources` | 설정 파일, 정적 리소스 | O |
| `src/main/resources/application.properties` | 앱 설정(DB 주소, 포트 등). 지금은 `spring.application.name=note`만 있어. | O |
| `src/main/resources/static`, `templates` | 정적 파일(HTML/CSS/JS)과 서버 템플릿. REST API만 만드는 지금은 안 써. | O |
| `src/test/java` | 테스트 코드 | O |
| `build/` | 컴파일 결과물. 빌드하면 다시 생겨. | X |
| `.gradle/` | 프로젝트별 Gradle 캐시 | X |
| `.vscode/` | VS Code 개인 설정 | X |
| `.gitignore` | Git에서 제외할 파일 목록 | O |
| `.gitattributes` | 줄바꿈 문자 규칙. `gradlew`는 LF, `.bat`은 CRLF를 유지해야 각 OS에서 실행돼. | O |
| `HELP.md` | Initializr가 만든 참고 링크 모음. `.gitignore`에 들어 있어. | X |

### 4.1 `build/`와 `bin/`이 둘 다 생기는 이유
- `build/`: **Gradle**이 컴파일한 결과물이야. `.\gradlew.bat` 명령을 실행하면 생겨.
- `bin/`: **VS Code Java 확장**이 내부 컴파일러(Eclipse JDT)로 따로 컴파일한 결과물이야. 에디터에서 에러 표시나 실행 버튼을 쓸 때 사용돼.
- 즉 **컴파일러가 두 개** 돌고 있어. 그래서 VS Code에서는 에러가 없는데 `gradlew`로는 실패하는 경우(또는 그 반대)가 생길 수 있어. 최종 판단 기준은 항상 `.\gradlew.bat` 결과야.
- 둘 다 `.gitignore`에 포함돼 있어.
- `build/resolvedMainClassName`: `bootRun`이나 `bootJar`를 실행할 때 Gradle이 찾아낸 main 클래스 이름을 기록한 파일이야. 이 파일이 있다는 건 해당 태스크가 한 번 실행됐다는 뜻이야.

### 4.2 소스 세트(Source Set): `src/main`과 `src/test`는 다른 세계야
- Gradle은 `src/main/java`와 `src/test/java`를 **별도의 소스 세트**로 컴파일해. 각각 사용할 수 있는 라이브러리 목록(클래스패스, Classpath)이 달라.

| 소스 세트 | 볼 수 있는 라이브러리 |
|---|---|
| `main` | `implementation`, `runtimeOnly`(실행 시) |
| `test` | `main`의 모든 것 + `testImplementation`, `testRuntimeOnly` + `main`의 클래스들 |

- 그래서 **테스트 파일을 `src/main`에 만들면** JUnit(`@Test`)과 AssertJ(`assertThat`)를 찾을 수 없어. 이 라이브러리들은 `testImplementation`이라서 main에서는 아예 보이지 않거든.
- 이 경우 VS Code의 빠른 수정(Quick Fix, 노란 전구)에도 import 제안이 안 나와. 해당 소스 세트의 클래스패스에 그 클래스가 없으니까 추천할 후보가 없는 거야.
- 테스트 파일은 **같은 패키지 경로를 `src/test/java` 아래에** 만들어야 해.
  - 운영 코드: `src/main/java/com/sunghyun/note/domain/Note.java`
  - 테스트 코드: `src/test/java/com/sunghyun/note/domain/NoteTest.java`
- 패키지가 같으면 테스트에서 `Note`를 import 없이 쓸 수 있고, `protected`나 package-private 멤버에도 접근할 수 있어.

### 4.3 VS Code import 단축키
- `Ctrl + .`: 커서 위치의 빠른 수정(Quick Fix). 노란 전구와 같은 기능이야.
- `Shift + Alt + O`: 파일 전체의 import 정리(Organize Imports). 빠진 import를 추가하고 안 쓰는 import는 지워.
- 둘 다 Java 언어 서버가 프로젝트를 인식한 상태에서만 동작해. 상태 표시줄에 `Java: Ready`가 보여야 해.

### 4.4 정적 임포트(Static Import)
- `assertThat`은 `org.assertj.core.api.Assertions` 클래스의 **static 메서드**야. 원래는 `Assertions.assertThat(...)`처럼 클래스 이름을 붙여서 호출해야 해.
- `import static org.assertj.core.api.Assertions.assertThat;`을 쓰면 클래스 이름 없이 `assertThat(...)`만으로 호출할 수 있어.
- 일반 `import`는 **클래스**를 가져오고, `import static`은 클래스 안의 **static 멤버(메서드, 상수)**를 가져와.
- 이게 없으면 컴파일러는 `assertThat`을 현재 클래스(`NoteTest`)에 있는 메서드로 찾아. 그래서 에러 메시지가 `The method assertThat(String) is undefined for the type NoteTest`("NoteTest에 그런 메서드가 없다")로 나와.
- `Shift+Alt+O`(Organize Imports)는 static import를 자동으로 추가해주지 않는 경우가 많아. 그럴 때는 직접 입력하거나, `Ctrl + .`의 "Add static import"를 써.
- `import static org.assertj.core.api.Assertions.*;`처럼 `*`로 전부 가져올 수도 있어. 편하지만 어떤 메서드가 어디서 왔는지 코드만 보고는 알기 어려워져서, 학습 중에는 하나씩 적는 걸 추천해.

#### 4.4.1 왜 클래스는 전구로 import되는데 `assertThat`은 안 되나
- **클래스 이름이 없을 때** (`Test`, `List` 등)
  - 컴파일러는 문법 위치를 보고 "이건 **타입 이름**이다"라는 걸 알아.
  - VS Code Java(Eclipse JDT 기반)는 클래스패스에 있는 **모든 클래스를 이름으로 색인(Index)**해두고 있어. 그래서 같은 이름의 클래스를 찾아서 import를 제안할 수 있어.
- **이름만 있는 메서드 호출일 때** (`assertThat(...)`)
  - Java 규칙상, 앞에 아무것도 붙지 않은 메서드 호출은 **현재 클래스 → 부모 클래스 → 바깥 클래스 → static import** 순서로 찾아.
  - 그래서 에러가 "NoteTest에 이 메서드가 없다"로 나오고, 빠른 수정도 기본적으로는 "NoteTest에 `assertThat` 메서드를 만들까?"를 제안해.
  - 언어 서버는 모든 클래스의 static 메서드까지 이름으로 뒤져서 제안하지는 않아. `assertThat`만 해도 AssertJ, Hamcrest 등 여러 라이브러리에 같은 이름이 있어서 후보가 애매하거든.
- **해결: 즐겨찾기 static 멤버 등록**
  - `java.completion.favoriteStaticMembers` 설정에 등록한 클래스의 static 멤버는 자동완성(`Ctrl+Space`)과 빠른 수정의 후보로 쓰여.
  - 기본값에는 JUnit 5의 `Assertions`와 Mockito는 들어 있지만 **AssertJ는 없어.** 그래서 직접 추가해야 해.
  - 이 설정은 기본값 목록을 **덮어쓰는** 방식이라, 기본값에 있던 항목도 같이 적어줘야 해.
- C#의 `using static`도 같은 개념이고, Visual Studio에서도 비슷한 제약이 있어.

### 4.5 Java는 대소문자를 구분해
- `isEqualTo`와 `isequalTo`는 완전히 다른 이름이야. 메서드 이름은 보통 카멜 케이스(camelCase)로, 단어가 시작될 때마다 대문자를 써.
- 직접 타이핑할 때는 자동완성(`Ctrl + Space`)으로 고르면 이런 오타를 막을 수 있어.

### 4.6 `BUILD SUCCESSFUL`을 믿어도 되나: 테스트 결과 읽는 법
- `BUILD SUCCESSFUL`은 "**실행된** 태스크 중에 실패한 게 없다"는 뜻이야. "내 코드가 맞다"는 뜻이 아니야.
- 성공처럼 보여도 실제로는 확인이 안 된 경우
  1. **테스트가 아예 실행되지 않음**: 테스트 파일이 `src/main`에 있거나, 저장이 안 됐거나, `@Test`가 빠졌으면 테스트 개수가 0개인데도 성공으로 나와.
  2. **이전 결과를 재사용함**: 코드가 바뀌지 않았으면 Gradle이 테스트를 건너뛰고 `UP-TO-DATE`로 표시해. 이건 정상 동작이고, 강제로 다시 돌리려면 `.\gradlew.bat test --rerun`을 써.
  3. **테스트가 검증하지 않는 부분이 틀림**: 테스트는 **작성한 것만** 검증해. 예를 들어 도메인 테스트는 JPA 매핑(`@Entity`)을 검사하지 않아.
- 확인하는 방법
  - **HTML 리포트**: `build/reports/tests/test/index.html`을 브라우저로 열면 테스트 개수, 성공/실패, 실행 시간, 그리고 각 테스트의 출력 로그(Standard output 탭)를 볼 수 있어.
  - **터미널에 테스트별 결과 출력**: `build.gradle`의 `test` 블록에 `testLogging { events "passed", "skipped", "failed" }`를 추가하면 테스트마다 PASSED/FAILED가 찍혀.
  - **일부러 깨뜨려보기**: 예를 들어 `>`를 `>=`로 바꿔서 테스트가 실제로 실패하는지 확인해. 실패해야 할 때 실패하지 않으면, 그 테스트는 아무것도 검증하지 못하고 있는 거야.

---

## 5. 앱의 시작점

### 5.1 `NoteApplication.java`
```java
@SpringBootApplication
public class NoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }
}
```
- `main`: 자바 프로그램의 진입점(Entry Point)이야.
- `SpringApplication.run(...)`: Spring 컨테이너(ApplicationContext)를 만들고, Bean을 생성하고, 내장 톰캣을 띄워.
- `@SpringBootApplication`은 세 가지 어노테이션을 합친 거야.
  - `@SpringBootConfiguration`: 이 클래스가 설정 클래스라는 표시
  - `@EnableAutoConfiguration`: 자동 설정을 켬 (3.1절)
  - `@ComponentScan`: **이 클래스가 있는 패키지(`com.sunghyun.note`)와 그 하위 패키지**에서 `@Component`, `@Service`, `@RestController` 같은 클래스를 찾아 Bean으로 등록
- 그래서 controller나 service 클래스를 `com.sunghyun.note` **바깥** 패키지에 만들면, Spring이 찾지 못해서 등록되지 않아. 에러 없이 조용히 무시되니까 주의해야 해.

### 5.2 `NoteApplicationTests.java`
```java
@SpringBootTest
class NoteApplicationTests {
    @Test
    void contextLoads() { }
}
```
- 내용이 비어 있지만 의미가 있는 테스트야. `@SpringBootTest`가 앱 전체의 Spring 컨테이너를 띄워보고, **설정 오류나 Bean 생성 실패가 없으면 통과**해.
- 앞으로 설정을 잘못 건드리면 이 테스트가 가장 먼저 깨져.

---

## 6. 환경 주의사항

### 6.1 OneDrive 안에 Git 저장소를 두면 생기는 문제
- OneDrive는 파일을 하나씩 따로 동기화해. 그런데 `.git/` 폴더는 수많은 파일이 **한 묶음으로 일관성**을 유지해야 하는 구조야.
- 동기화 도중에 일부 파일만 바뀐 상태가 되거나, 다른 PC와 충돌 사본(`index-PC이름.lock` 같은 파일)이 생기면 저장소가 손상될 수 있어.
- Git 자체가 버전 관리 도구니까, 백업은 OneDrive 대신 GitHub 같은 원격 저장소(Remote)에 push해서 하는 게 정석이야.

---

## 7. Git과 GitHub

### 7.1 로컬 저장소와 원격 저장소
- **로컬 저장소(Local Repository)**: 내 PC의 `.git/` 폴더. `commit`은 여기에만 기록돼.
- **원격 저장소(Remote Repository)**: GitHub 같은 서버에 있는 저장소. `push`를 해야 올라가.
- 즉 커밋만 하고 push하지 않으면 PC가 고장 났을 때 기록이 같이 사라져.

### 7.1.1 `git status`로 보는 파일의 상태
```
작업 폴더(Working Directory) --git add--> 스테이징 영역(Staging Area) --git commit--> 저장소(.git)
```
- **Untracked**: Git이 아직 한 번도 기록하지 않은 새 파일이야. (예: `CLAUDE.md`, `docs/`)
- **Modified (not staged)**: 이미 기록된 파일인데 내용이 바뀌었고, 아직 `git add`를 하지 않은 상태야. (예: `build.gradle`)
- **Staged**: `git add`로 "다음 커밋에 넣을 것"으로 표시된 상태야.
- **스테이징 영역이 왜 있나**: 바뀐 파일 중 **일부만 골라서** 커밋하려고 있는 거야. 그래서 "한 커밋 = 한 가지 의미"를 지킬 수 있어. `git add .`은 전부 다 넣는 거야.
- **`.gitignore`에 있는 파일**(`build/`, `.vscode/` 등)은 status에 아예 안 나와.

### 7.2 연결 명령어
```powershell
git remote add origin https://github.com/<계정>/<저장소>.git
git branch -M main
git push -u origin main
```
- `remote add origin <URL>`: 원격 저장소 주소에 `origin`이라는 별명을 붙여 등록해. `origin`은 관례적인 이름일 뿐 특별한 키워드가 아니야.
- `branch -M main`: 현재 브랜치 이름을 `main`으로 바꿔. Git 기본값은 `master`이고 GitHub 기본값은 `main`이라서 맞춰주는 거야.
- `push -u origin main`: 로컬 `main`을 원격 `origin`의 `main`으로 올려. `-u`(upstream)는 "이 둘을 짝으로 기억해라"라는 뜻이야. 한 번 지정하면 다음부터는 `git push`만 쳐도 돼.

### 7.3 인증
- Windows용 Git에는 **Git Credential Manager**가 포함돼 있어. 처음 push할 때 브라우저가 열리고 GitHub 로그인을 하면, 인증 정보가 Windows 자격 증명 관리자에 저장돼. 그다음부터는 묻지 않아.
- GitHub는 비밀번호로 push하는 방식을 막았어. 그래서 브라우저 로그인이나 토큰(PAT, Personal Access Token) 같은 다른 인증 방식이 필요해.

### 7.4 GitHub에서 빈 저장소를 만들어야 하는 이유
- GitHub에서 저장소를 만들 때 README, .gitignore, license를 체크하면, 원격 저장소에 첫 커밋이 하나 생겨.
- 그러면 로컬 저장소와 원격 저장소가 **서로 관계없는 두 개의 역사(Unrelated Histories)**를 갖게 돼서 push가 거부돼.
- 이미 로컬에 커밋이 있으면 원격 저장소는 비워둔 채로 만들어야 해.

---

## 8. Claude Code의 CLAUDE.md 로딩 규칙
- Claude Code는 **시작한 폴더(작업 디렉터리)와 그 상위 폴더**에 있는 `CLAUDE.md`를 세션 시작 시 자동으로 읽어.
- **하위 폴더**(예: `docs/CLAUDE.md`)에 있는 건 시작할 때 읽지 않아. Claude가 그 하위 폴더 안의 파일을 다룰 때만 추가로 읽어.
- 그래서 프로젝트 전체 규칙은 반드시 **루트**에 둬야 해. 하위 폴더의 CLAUDE.md는 "이 폴더에서만 적용되는 규칙"을 둘 때 쓰는 거야.

---

## 9. 파일 잠금과 Gradle 데몬

### 9.1 폴더 이동이 일부만 되는 이유
- Windows에서는 프로그램이 열어둔 파일을 이동하거나 삭제할 수 없어. 이걸 파일 잠금(File Lock)이라고 해.
- 이번 경우에는 `.gitignore`, `gradlew.bat`, `CLAUDE.md`만 옮겨지고 `.git`, `src`, `build.gradle` 등은 `note/`에 남았어. 다른 프로그램이 잡고 있던 파일에서 이동이 멈춘 거야.
- 잠금을 거는 주체
  - **VS Code Java 확장**: 프로젝트를 분석하려고 `build.gradle`, `src`, `bin/`을 열어둬.
  - **Gradle 데몬(Daemon)**: 아래 9.2절
  - **OneDrive**: 동기화하는 중인 파일을 잠가.

### 9.2 Gradle 데몬
- 데몬은 **백그라운드에 계속 떠 있는 프로세스**야. Gradle은 처음 실행할 때 JVM을 띄우고 빌드 정보를 메모리에 올려두는데, 이게 느려. 그래서 빌드가 끝나도 프로세스를 종료하지 않고 남겨둬서 다음 빌드를 빠르게 해.
- **VS Code를 닫아도 데몬은 남아 있어.** 이게 파일 이동이 실패하는 흔한 원인이야.
- 종료 방법: `.\gradlew.bat --stop` 명령을 쓰거나, 작업 관리자에서 `OpenJDK Platform binary`(Java) 프로세스를 종료하면 돼. PC를 재부팅해도 돼.

### 9.3 옮기지 않고 지워도 되는 폴더
- `build/`, `bin/`, `.gradle/`은 **다시 만들 수 있는 산출물**이야. 빌드하면 자동으로 다시 생겨.
- 옮길 필요 없이 지우면 돼. 이 폴더들은 잠금이 가장 잘 걸리는 곳이라서, 지우는 게 이동보다 실패가 적어.

---

## 10. `application.properties` 설정

```properties
spring.datasource.url=jdbc:h2:mem:notedb
spring.h2.console.enabled=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

| 설정 | 무엇 | 왜 넣었나 |
|---|---|---|
| `spring.datasource.url` | 접속할 DB 주소. `mem:`은 메모리 모드, `notedb`는 DB 이름이야. | 이 값이 없으면 Spring Boot가 실행할 때마다 랜덤 이름(`testdb-xxxx`)의 DB를 만들어. 그러면 H2 콘솔에서 어디로 접속해야 할지 매번 로그를 찾아봐야 해. |
| `spring.h2.console.enabled` | `/h2-console` 웹 화면을 켜. | 기본값은 꺼져 있어. 콘솔이 있어야 저장된 데이터를 눈으로 확인할 수 있어. |
| `spring.jpa.show-sql` | Hibernate가 실행하는 SQL을 콘솔에 출력해. | JPA는 SQL을 대신 만들어주기 때문에, 출력하지 않으면 실제로 무슨 쿼리가 나가는지 알 수 없어. |
| `hibernate.format_sql` | 출력되는 SQL을 줄바꿈해서 읽기 좋게 만들어. | 학습용 |

- H2 콘솔 접속 정보: JDBC URL은 `jdbc:h2:mem:notedb`, User Name은 `sa`, Password는 빈 칸이야. 둘 다 Spring Boot 기본값이야.
- 메모리 모드라서 **앱을 끄면 데이터가 사라져.** 개발 중에는 오히려 매번 깨끗한 상태로 시작할 수 있어서 편해.
- `show-sql`은 `System.out`으로 출력하는 방식이라 운영 환경에서는 쓰지 않아. 운영에서는 로깅 설정(`logging.level.org.hibernate.SQL=debug`)을 써.

---

## 11. JPA 엔티티 (`Note.java`)

### 11.1 어노테이션
| 어노테이션 | 무엇 | 내부적으로 하는 일 |
|---|---|---|
| `@Entity` | "이 클래스는 DB 테이블과 매핑된다"는 표시 | Hibernate가 앱 시작 시 이 클래스를 찾아서 메타데이터(테이블 이름, 컬럼)를 만들어. 개발 중에는 이걸 보고 `create table note (...)`를 자동으로 실행해(`ddl-auto`). |
| `@Id` | 기본 키(Primary Key) 필드 | 엔티티를 구분하는 기준. `find`, `save`가 이 값을 기준으로 동작해. |
| `@GeneratedValue(strategy = IDENTITY)` | 키 값을 DB가 자동 증가로 만들게 함 | `INSERT`할 때 id를 비워서 보내고, DB가 만든 값을 다시 읽어와 필드에 채워. |
| `@Column(nullable = false, length = 100)` | 컬럼 제약 | 테이블을 만들 때 `NOT NULL`, `varchar(100)`으로 생성돼. **DB 제약일 뿐, 자바 객체 검증은 하지 않아.** |
| `@PrePersist` / `@PreUpdate` | 생명주기 콜백(Lifecycle Callback) | Hibernate가 `INSERT` 직전, `UPDATE` 직전에 이 메서드를 자동으로 호출해. |

### 11.2 `protected Note() {}` 기본 생성자가 왜 필요한가
- Hibernate는 DB에서 데이터를 조회할 때 **먼저 빈 객체를 만들고**(기본 생성자 호출), 그다음 리플렉션(Reflection)으로 필드에 값을 채워.
- 그래서 매개변수가 없는 생성자가 반드시 있어야 해.
- `public`이 아니라 `protected`로 두는 이유: 다른 개발자(그리고 AI)가 `new Note()`로 **검증을 건너뛴 빈 노트**를 만드는 걸 막으려고. Hibernate는 protected도 접근할 수 있어.

### 11.3 검증을 두 군데서 하는 이유 (도메인 vs DTO)
- **DTO 검증(`@Valid`, `@NotBlank`)**: API 입구에서 잘못된 요청을 걸러내고, 사용자에게 400 에러와 친절한 메시지를 돌려줘.
- **도메인 검증(생성자 안의 `if` + 예외)**: `Note` 객체가 **어떤 경로로 만들어지든** 항상 규칙을 지키게 보장해. 이렇게 객체가 항상 지켜야 하는 규칙을 **불변식(Invariant)**이라고 해.
- DTO 검증만 있으면, 나중에 API가 아닌 다른 경로(배치 작업, 테스트 코드 등)로 노트를 만들 때 규칙이 깨질 수 있어.

### 11.4 Setter를 만들지 않는 이유
- `setTitle()`이 있으면 아무 곳에서나 제목을 바꿀 수 있고, 그때마다 검증을 우회할 수 있어.
- 그래서 값을 바꾸는 건 의미 있는 이름의 메서드(예: 나중에 만들 `update(title, content)`)로만 허용하고, 그 안에서 다시 검증해.
- 이게 CLAUDE.md의 "비즈니스 로직은 domain에 둔다"는 규칙의 구체적인 모습이야.

### 11.5 테스트 메서드 이름을 한글로 쓰는 것
- Java는 식별자(Identifier)에 유니코드를 허용해서 한글 메서드 이름도 문법상 문제가 없어.
- 테스트 이름은 **문서 역할**을 하니까, 읽기 쉬운 한글이 유리해. 단, 운영 코드(main)에서는 영어를 쓰는 게 관례야.

---

## 질문 기록

대화 중에 나온 질문과 답을 날짜순으로 쌓는다.

<!-- 형식
### YYYY-MM-DD 질문 제목
- 질문:
- 답 요약:
- 내가 헷갈렸던 점:
-->

### 2026-10-03 Java 버전을 27에서 25로 바꾸기
- 질문: 설치한 JDK가 25인데 프로젝트가 27로 생성됐다. 어떻게 바꾸나?
- 답 요약: `build.gradle`의 `JavaLanguageVersion.of(27)`을 `25`로 바꾸고, VS Code에서 Java 프로젝트를 다시 불러온다(`Java: Reload Projects`). 툴체인은 "컴파일할 JDK 버전"을 고정하는 설정이라, PC에 없는 버전을 적으면 빌드가 실패한다.
- 확인 명령: `java -version`(현재 터미널의 JDK), `.\gradlew.bat -version`(Gradle이 쓰는 JVM), `.\gradlew.bat javaToolchains`(Gradle이 찾은 JDK 목록)

### 2026-10-03 setup 점검 결과
- 질문: setup을 제대로 했는지, 빠진 게 있는지
- 답 요약: 컴파일, 앱 실행, Git 첫 커밋은 됨. 그런데 `test`를 실행한 흔적이 없고, Java 25 변경이 커밋되지 않았고, `CLAUDE.md`와 `docs/`가 Git 밖에 있음.
- 배운 점: 폴더 안의 파일(`build/test-results` 유무, `.git/index`)만 봐도 어떤 명령이 실행됐는지 추적할 수 있다.

### 2026-10-03 CLAUDE.md 위치와 GitHub 연동
- 질문: CLAUDE.md를 docs로 옮겼다. 그리고 내 GitHub에 연동할 수 있나?
- 답 요약: CLAUDE.md는 루트에 있어야 세션 시작 시 자동으로 로드된다(8절). GitHub 연동은 빈 저장소 생성 → `remote add` → `push -u` 순서로 한다(7절). 폴더 구조를 정리한 다음에 push한다.

### 2026-10-03 폴더 이동이 절반만 됨
- 상황: `.gitignore`, `gradlew.bat`, `CLAUDE.md`만 루트로 옮겨지고, `.git`, `src`, `build.gradle`, `gradle/`, `gradlew`, `settings.gradle`, `.gitattributes`는 `note/`에 남았다.
- 원인(추정): VS Code 또는 Gradle 데몬이 파일을 잠그고 있었다(9절).
- 영향: 지금 상태에서는 아무것도 동작하지 않는다. `gradlew.bat`은 루트에 있는데 `gradle/wrapper/`는 `note/`에 있어서 Gradle을 실행할 수 없고, `.gitignore`는 `.git`과 다른 폴더에 있어서 적용되지 않는다.
- 해결: VS Code와 Java 프로세스를 종료한 뒤, 탐색기에서 나머지 항목을 옮기고 산출물 폴더는 삭제한다.

### 2026-10-03 폴더 구조 정리 완료
- 결과: `.git`, `CLAUDE.md`, `build.gradle`, `src`, `docs`가 모두 루트에 모였다. `note/` 폴더 삭제 완료.
- 추가 작업: `application.properties`에 H2와 SQL 로그 설정을 추가했다(10절).
- 다음 단계: `test` 실행 → 커밋 → GitHub 원격 저장소 연결(7절)

### 2026-10-03 쓰고 있는 기술을 어떻게 분류하나
- 질문: 무엇을 쓰는지는 알겠는데, 그것들을 백엔드/프론트엔드/언어 등으로 어떻게 분류하는지 모르겠다.
- 답 요약: "어디서 실행되나 / 어떤 층인가 / 언제 쓰이나" 세 가지 기준으로 따로 나눈다(0.5절). 현재 프로젝트는 백엔드만 있고 프론트엔드는 아직 없다.
- 헷갈렸던 점: 프레임워크와 라이브러리의 차이 → 누가 누구를 호출하느냐(제어의 역전)

### 2026-10-03 진행 방식 변경
- 결정: Claude가 코드를 제시하고, 내가 직접 타이핑하면서 이해한다. Claude Code의 자동 작성은 당분간 쓰지 않는다.

### 2026-10-03 노란 전구를 눌러도 import 제안이 안 나옴
- 상황: `NoteTest.java`에서 `@Test`, `assertThat`에 빨간 밑줄이 생겼고, 빠른 수정에도 import 제안이 없었다.
- 원인 1: `NoteTest.java`를 `src/main/java/.../domain/`에 만들었다. JUnit과 AssertJ는 `testImplementation`이라 main에서는 보이지 않는다(4.2절).
- 원인 2: `Note.java`와 `NoteTest.java`가 디스크에 0바이트였다. 저장(`Ctrl+S`)하지 않은 상태라서, Gradle에는 빈 파일로 보인다.
- 해결: 두 파일을 저장하고, `NoteTest.java`를 `src/test/java/com/sunghyun/note/domain/`으로 옮긴다.
- 배운 점: 빨간 밑줄의 원인이 "import가 없다"가 아니라 "그 클래스를 볼 수 없는 위치에 있다"일 수도 있다.

### 2026-10-03 `assertThat(String) is undefined for the type NoteTest`
- 원인 1: `assertThat`의 정적 임포트(`import static ...Assertions.assertThat;`)가 빠졌다(4.4절).
- 원인 2: 11번째 줄에 `isequalTo`라고 오타를 냈다. 정확한 이름은 `isEqualTo`다(4.5절).
- 배운 점: 에러 메시지의 "for the type NoteTest"는, 컴파일러가 그 메서드를 NoteTest 안에서 찾다가 실패했다는 뜻이다.

### 2026-10-03 테스트는 전부 통과했는데 `@Entity`가 빠져 있었다
- 상황: `.\gradlew.bat test`는 `BUILD SUCCESSFUL`. `NoteTest` 6개, `contextLoads` 1개 모두 통과.
- 발견: `Note.java`에 `@Entity`와 그 import가 빠져 있었다. 그래서 Hibernate가 Note를 엔티티로 인식하지 않았고, 로그에 `create table note` SQL이 없었다.
- 왜 테스트가 못 잡았나: `NoteTest`는 Spring과 JPA 없이 순수 자바 객체만 검사한다. `contextLoads`도 Note를 사용하는 Bean이 없으니 문제를 일으키지 않는다. **아무 테스트도 JPA 매핑을 검증하지 않았다.**
- 다음에 잡히는 곳: ② Repository 단계에서 `@DataJpaTest`로 실제 저장/조회 테스트를 쓰면, `@Entity`가 없을 때 바로 실패한다.
- 배운 점: `BUILD SUCCESSFUL`은 "작성한 테스트가 통과했다"일 뿐이다. 로그(여기서는 `create table`)처럼 **기대한 일이 실제로 일어났다는 증거**를 따로 확인해야 한다(4.6절).
