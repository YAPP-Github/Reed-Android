# Reed-Android
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.11.1-green.svg)](https://gradle.org/)
[![Android Studio](https://img.shields.io/badge/Android%20Studio-2025.1.2%20%28Narwhal%29-green)](https://developer.android.com/studio)
[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-28-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-35-orange)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/YAPP-Github/Reed-Android?utm_source=oss&utm_medium=github&utm_campaign=YAPP-Github%2FReed-Android&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)
<br/>

Reed(리드) - 문장과 감정을 함께 담는 독서 기록 [PlayStore](https://play.google.com/store/apps/details?id=com.ninecraft.booket&hl=ko)
<img width="1024" height="500" alt="reed_graphic" src="https://github.com/user-attachments/assets/9e4e6f0a-46b0-4409-b2f9-d3c5f0a3a8b1" />

<p align="center">
<img src="https://github.com/user-attachments/assets/f31d5681-bbf0-4de4-93a6-37a2adf54df7" width="30%"/>
<img src="https://github.com/user-attachments/assets/db92e159-091d-425b-8cc2-2324d4191463" width="30%"/>
<img src="https://github.com/user-attachments/assets/5fd3f726-f493-4850-9d99-3933815dcd8b" width="30%"/>
</p>
<p align="center">
<img src="https://github.com/user-attachments/assets/bb7e3281-c7e4-453d-a921-cc4e9a051434" width="30%"/>
<img src="https://github.com/user-attachments/assets/7c211781-58e5-4413-9f20-f92b1d0c8f24" width="30%"/>
</p>

## Features

## TroubleShooting
- [[Compose] M3 ModalBottomSheet 드래그(터치 이벤트) 막는 법](https://velog.io/@mraz3068/Compose-M3-ModalBottomSheet-Drag-Disabled)
- [Circuit 찍먹해보기(부제: Circuit 희망편)](https://speakerdeck.com/easyhooon/circuit-jjigmeoghaebogi-buje-circuit-hyimangpyeon)
- [Circuit 찍먹해보기(부제: Circuit 절망편)](https://speakerdeck.com/easyhooon/circuit-jjigmeoghaebogi-buje-circuit-jeolmangpyeon)
- [Jetpack Compose에서 CameraX + MLKit으로 OCR을 구현해보자](https://velog.io/@syoon513/Jetpack-Compose%EC%97%90%EC%84%9C-CameraX-MLKit%EC%9C%BC%EB%A1%9C-OCR%EC%9D%84-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90)
- [[Android] 일회성 이벤트를 StateFlow, Compose의 State로 처리할 때 주의해야할 점](https://velog.io/@mraz3068/Handle-One-Time-Event-As-State)
- [Circuit Navigation 사용 시 feature 모듈간의 참조는 어떻게 해결했을까?](https://velog.io/@syoon513/Circuit-Navigation-%EC%82%AC%EC%9A%A9-%EC%8B%9C-feature-%EB%AA%A8%EB%93%88%EA%B0%84-%EC%88%9C%ED%99%98-%EC%B0%B8%EC%A1%B0%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%B4%EA%B2%B0%ED%96%88%EC%9D%84%EA%B9%8C)
- [Coroutine 에러 처리 패턴: 여러 API 호출을 한 번에 성공/실패 판정하기](https://velog.io/@syoon513/Coroutine-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC)
- [[Circuit] ImpressionEffect](https://velog.io/@mraz3068/Circuit-ImpressionEffect)

## Development

### Required

- IDE : Android Studio 최신 버전
- JDK : Java 17을 실행할 수 있는 JDK
    - (권장) Android Studio 설치 시 Embeded 된 JDK (Open JDK)
    - Java 17을 사용하는 JDK (Open JDK, AdoptOpenJDK, GraalVM)     
- Kotlin Language : 2.2.0

### Language

- Kotlin

### Libraries

- AndroidX
  - Activity Compose
  - Core
  - DataStore
  - StartUp
  - Splash
  - Camera

- Kotlin Libraries (Coroutine, Serialization, Immutable Collection)
- Compose
  - Material3

- [Circuit](https://github.com/slackhq/circuit)
- Dagger Hilt
- Retrofit, OkHttp
- Coil-Compose, [Landscapist](https://github.com/skydoves/landscapist)
- Google-ML Kit
- Lottie-Compose
- Firebase(Analytics, Crashlytics, Remote Config)
- Kakao-Auth
- [Logger](https://github.com/orhanobut/logger)
- [Compose-Stable-Marker](https://github.com/skydoves/compose-stable-marker)
- [Landscapist](https://github.com/skydoves/landscapist), Coil-Compose
- [ComposeExtensions](https://github.com/taehwandev/ComposeExtensions)
- [compose-effects](https://github.com/skydoves/compose-effects)
- [compose-shadow](https://github.com/adamglin0/compose-shadow)

#### Test & Code analysis

- Ktlint
- Detekt

#### Gradle Dependency

- Gradle Version Catalog

## Architecture
- Android App Architecture
- MVI

## Developers

|Android|Android|
|:---:|:---:|
|[이지훈](https://github.com/easyhooon)|[이서윤](https://github.com/seoyoon513)|
|<img width="144" src="https://github.com/user-attachments/assets/7e54768a-ce44-421d-8c03-9df7d0492855">|<img width="144" src="https://github.com/user-attachments/assets/a6cea688-cf9b-41ad-a3a0-9a0c11775fa2">|

## Module
<img width="1631" height="719" alt="image" src="https://github.com/user-attachments/assets/f6a26dcd-761a-4dab-ae3f-a32e87eb423b" />

## Package Structure
```
├── app
│   └── application
├── build-logic
├── core
│   ├── common
│   ├── data
│   ├── datastore
│   ├── designsystem
│   ├── model
│   ├── network
│   ├── ocr
│   └── ui
├── feature
│   ├── detail
│   ├── edit
│   ├── home
│   ├── library
│   ├── login
│   ├── main
│   ├── onboarding
│   ├── record
│   ├── screens
│   ├── settings
│   ├── splash
│   └── webview
├── gradle
    └── libs.versions.toml

```
<br/>
