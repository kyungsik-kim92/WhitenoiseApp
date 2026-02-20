# 🎵 WhiteNoise App
집중·휴식용 백색소음 재생 앱입니다. 비, 바람, 새소리 등 다양한 자연음을 제공하며, 여러 소리를 동시에 재생해 나만의 사운드 믹스를 만들 수 있습니다. 타이머를 설정하면 지정한 시간 후 자동으로 재생이 종료됩니다.

## 프로젝트 구조

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


## 주요 기능
- **다중 재생** — 비, 바람, 새소리, 파도, 차량 소리 등 여러 소리를 선택해 동시에 재생
- **타이머** — 제한 없음 / 1분 ~ 3시간 구간 설정 후 자동 종료, 일시정지·재개 지원
- **백그라운드 재생** — Android Service 기반으로 앱을 나가도 재생 유지
- **상태 동기화** — 앱을 백그라운드에서 복귀해도 실제 재생 상태에 맞춰 UI(체크 상태) 자동 반영
