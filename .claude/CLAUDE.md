# Hime 프로젝트 작업 지침

## 빌드 관련

- **빌드는 사용자가 직접 수행합니다**
- 기능 작업 완료 후 빌드를 자동으로 실행하지 마세요 (시간이 오래 걸림)
- 빌드가 필요한 경우 사용자에게 알리고 사용자가 직접 실행하도록 합니다

## 커밋 관련

- **커밋 메시지에서 Claude 관련 문구를 제거합니다**
- 다음 문구들을 커밋 메시지에 포함하지 마세요:
  - `🤖 Generated with [Claude Code](https://claude.com/claude-code)`
  - `Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>`
- 커밋 작업은 사용자가 직접 수행하는 경우가 많으므로, 요청받지 않은 경우 커밋하지 마세요

## 코드 작성 원칙

- 한글 주석 사용
- Kotlin 코딩 컨벤션 준수
- 기존 코드 스타일 유지