# 🎵 WhiteNoise App
집중·휴식용 백색소음 재생 앱입니다. 비, 바람, 새소리 등 다양한 자연음을 제공하며, 여러 소리를 동시에 재생해 나만의 사운드 믹스를 만들 수 있습니다. 타이머를 설정하면 지정한 시간 후 자동으로 재생이 종료됩니다.

## 주요 기능
- **다중 재생** — 비, 바람, 새소리, 파도, 차량 소리 등 여러 소리를 선택해 동시에 재생
- **타이머** — 제한 없음 / 1분 ~ 3시간 구간 설정 후 자동 종료, 일시정지·재개 지원
- **백그라운드 재생** — Android Service 기반으로 앱을 나가도 재생 유지
- **상태 동기화** — 앱을 백그라운드에서 복귀해도 실제 재생 상태에 맞춰 UI(체크 상태) 자동 반영

## 기술 스택
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** Clean Architecture + MVVM
- **Async:** Kotlin Coroutines + Flow
- **DI:** Hilt
- **Jetpack:** ViewModel, Navigation Compose, Lifecycle
- **Background:** Android Service, MediaPlayer, CountDownTimer
- **Image:** Coil
- **Build:** Gradle Kotlin DSL + Convention Plugin + Version Catalog
- **Testing:** JUnit, Mockito, Coroutines Test

- ## 프로젝트 구조

```
WhitenoiseApp/
├── app/                    # 앱 모듈 (진입점)
│   ├── ui/
│   │   ├── main/           # MainActivity, MainScreen, Navigator
│   │   ├── play/           # 재생 화면 (PlayScreen, PlayViewModel)
│   │   └── timer/          # 타이머 화면
│   ├── di/                 # Hilt 모듈
│   └── constants/          # PlayList, TimerList 상수
├── domain/                 # 도메인 레이어
│   ├── model/              # PlayModel, TimerModel, TimerState
│   ├── repository/         # Repository 인터페이스
│   └── usecase/            # UseCase (비즈니스 로직)
├── data/                   # 데이터 레이어
│   ├── repository/         # Repository 구현
│   ├── datasource/
│   │   ├── local/          # PlayLocalDataSource, TimerLocalDataSource
│   │   └── service/        # MediaPlayerDataSource
│   └── service/            # WhiteNoiseService
├── core/                   # 공통 모듈
│   ├── ui/                 # 테마(Colors), 컴포넌트(GradientBackground, AnimatedCard 등)
│   ├── extensions/        # Flow 확장
│   └── utils/              # TimeFormatter
└── build-logic/            # Convention Plugins (application, library, Hilt)
```


## 기술적 도전과 해결

**백그라운드 재생 끊김**  
`bindService`만 사용하면 unbind 시 Service가 종료되는 현상. `onStart`에서 `startService`로 Service를 먼저 시작한 뒤 `bindService`로 통신하고, `onDestroy(isFinishing)`에서만 `stopService`로 종료하도록 변경.

**앱 복귀 시 UI와 재생 상태 불일치**  
ViewModel이 상수 목록만 참조해 앱 복귀 시 UI와 실제 재생 상태가 어긋나던 문제입니다. Service에 재생 중인 인덱스 목록 API를 추가하고, `SyncPlayStateUseCase`로 Repository와 동기화한 뒤, 서비스 준비 시 ViewModel에서 해당 UseCase를 호출하도록 변경.

**Convention Plugin + Version Catalog**  
모듈마다 반복되던 Kotlin·Compose·Hilt 설정을 `build-logic`의 Convention Plugin으로 묶고, 라이브러리 버전은 Version Catalog로만 관리해 의존성과 설정을 한 곳에서 제어하도록 변경.

**모듈 분리 + Compose 전환**  
기존 Fragment/Adapter 구조를 제거하고, app·domain·data·core로 모듈을 나눈 뒤 Jetpack Compose로 전환해 단방향 의존과 화면 단위 구조를 통일.

## 테스트

- **domain:** UseCase 단위 테스트 (JUnit, Mockito, Coroutines Test)
- FormatTimeUseCase, GetTimerListUseCase, SelectTimerUseCase, ObserveTimerStateUseCase, GetSelectedTimerUseCase, TogglePlaySelectionUseCase

## 음원 출처

앱 내 사용 음원은 [공유마당](https://gongu.copyright.or.kr)(한국저작권위원회)에서 제공하는 공유저작물을 사용하였습니다.  
CC BY(저작권정보 표시) 라이선스에 따라 출처를 표기합니다.
