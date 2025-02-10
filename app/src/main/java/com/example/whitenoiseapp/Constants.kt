package com.example.whitenoiseapp

object Constants {
    fun getPlayList(): List<PlayModel> {
        return listOf(
            PlayModel(
                musicResId = R.raw.baseline_water,
                iconResId = R.drawable.baseline_water_24
            ),
            PlayModel(
                musicResId = R.raw.bird,
                iconResId = R.drawable.bird
            ),
            PlayModel(
                musicResId = R.raw.frog,
                iconResId = R.drawable.frog
            ),
            PlayModel(
                musicResId = R.raw.insect,
                iconResId = R.drawable.insect
            ),
            PlayModel(
                musicResId = R.raw.river,
                iconResId = R.drawable.river
            ),
            PlayModel(
                musicResId = R.raw.car,
                iconResId = R.drawable.round_directions_car_24
            ),
            PlayModel(
                musicResId = R.raw.flight,
                iconResId = R.drawable.round_flight_takeoff_24
            ),
            PlayModel(
                musicResId = R.raw.forest,
                iconResId = R.drawable.round_forest_24
            ),
            PlayModel(
                musicResId = R.raw.fire,
                iconResId = R.drawable.round_local_fire_department_24
            ),
            PlayModel(
                musicResId = R.raw.rain_roof,
                iconResId = R.drawable.round_roofing_24
            ),
            PlayModel(
                musicResId = R.raw.rain_roof,
                iconResId = R.drawable.round_train_24
            ),
            PlayModel(
                musicResId = R.raw.rain_umbrella,
                iconResId = R.drawable.round_umbrella_24
            ),
            PlayModel(
                musicResId = R.raw.rain_default,
                iconResId = R.drawable.round_water_drop_24
            ),
            PlayModel(
                musicResId = R.raw.rain_window,
                iconResId = R.drawable.round_window_24
            ),
            PlayModel(
                musicResId = R.raw.ship,
                iconResId = R.drawable.ship
            ),
            PlayModel(
                musicResId = R.raw.wind,
                iconResId = R.drawable.wind
            ),
            PlayModel(
                musicResId = R.raw.musicbox1,
                iconResId = R.drawable.musicbox
            ),
        )
    }

    fun getTimerList(): List<TimerModel> {
        return listOf(
            TimerModel(
                timerStr = "No time limit",
                ms = 0
            ),
            TimerModel(
                timerStr = "1 minute",
                ms = 60000
            ),
            TimerModel(
                timerStr = "5 minutes",
                ms = 300000
            ),
            TimerModel(
                timerStr = "10 minutes",
                ms = 600000
            ),
            TimerModel(
                timerStr = "15 minutes",
                ms = 900000
            ),
            TimerModel(
                timerStr = "20 minutes",
                ms = 1200000
            ),
            TimerModel(
                timerStr = "30 minutes",
                ms = 1800000
            ),
            TimerModel(
                timerStr = "1 hour",
                ms = 3600000
            ),
            TimerModel(
                timerStr = "2 hours",
                ms = 7200000
            ),
            TimerModel(
                timerStr = "3 hours",
                ms = 10800000
            ),
        )
    }
}