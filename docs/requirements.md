# 요구사항 (초안 - 확정 전)

## MVP 범위

### Note
| 필드 | 타입 | 규칙 |
|---|---|---|
| id | Long | 자동 생성 |
| title | String | 필수, 1~100자 |\
| content | String | 마크다운 본문, 빈 값 허용 |
| tags | Set<String> | 0개 이상, 태그당 1~20자 |
| createdAt | LocalDateTime | 생성 시 자동 |
| updatedAt | LocalDateTime | 수정 시 자동 |

### API
| 순서 | 메서드 | 경로 | 설명 |
|---|---|---|---|
| 1 | POST | /api/notes | 노트 생성 |
| 2 | GET | /api/notes/{id} | 노트 단건 조회 |
| 3 | GET | /api/notes | 노트 목록 (페이징) |
| 4 | PUT | /api/notes/{id} | 노트 수정 |
| 5 | DELETE | /api/notes/{id} | 노트 삭제 |
| 6 | GET | /api/notes?tag=spring | 태그로 필터 |
| 7 | GET | /api/notes?q=키워드 | 제목/본문 검색 |

순서대로 하나씩 구현한다.

## MVP에서 제외 (나중에)
- 로그인 / 사용자 구분
- 웹 UI (아티팩트로 시안 먼저)
- PostgreSQL 전환, Docker 배포
- MCP 서버로 노트 검색 노출
