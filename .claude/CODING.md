# 코딩 가이드

## 코드 작성 원칙

- 한글 주석 사용
- Kotlin 코딩 컨벤션 준수
- 기존 코드 스타일 유지
- 파일 끝에 빈 줄(newline) 추가

## Compose 관련

- **Composable 함수 내 Collection 타입**
    - `List`, `Set`, `Map` 등의 Collection 대신 `ImmutableList`, `ImmutableSet`, `ImmutableMap` 사용
    - `kotlinx.collections.immutable` 라이브러리 사용
    - 예시:
      ```kotlin
      // ❌ 사용하지 않음
      @Composable
      fun TripList(trips: List<Trip>) { ... }

      // ✅ 사용
      @Composable
      fun TripList(trips: ImmutableList<Trip>) { ... }
      ```
    - 변환 시 `toImmutableList()`, `persistentListOf()` 등 사용
