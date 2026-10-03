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
