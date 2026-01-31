# Reed 프로젝트 작업 지침

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

## MCP 설정 관련

- **Claude Code CLI의 MCP 설정 파일 위치**
    - Claude Desktop이 아니라 **Claude Code CLI**를 사용 중입니다
    - MCP 설정은 `~/.claude.json`의 `projects` 섹션에서 프로젝트별로 관리됩니다
    - Claude Desktop 설정 파일(`~/Library/Application Support/Claude/claude_desktop_config.json`)을 수정하지 마세요
- **Figma MCP 설정 경로**
    - `~/.claude.json` → `projects` → `/Users/medi/AndroidStudioProjects/YeoBee-Android` → `mcpServers` → `figma`
