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
