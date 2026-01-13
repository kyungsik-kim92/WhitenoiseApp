package com.example.whitenoiseapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whitenoiseapp.R
import com.example.whitenoiseapp.ui.play.PlayScreen
import com.example.whitenoiseapp.ui.timer.TimerScreen

@Composable
fun MainScreen() {
    val mainNavigator = rememberMainNavigator()
    val mainViewModel = hiltViewModel<MainViewModel>()

    Scaffold(
        modifier = Modifier.background(Color(0xFF2A5D61)),
        bottomBar = {
            MainBottomBar(mainNavigator)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2A5D61))
        ) {
            TopGreenHeader()

            NavHost(
                navController = mainNavigator.navController,
                startDestination = mainNavigator.startDestination,
                modifier = Modifier.padding(
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = paddingValues.calculateBottomPadding()
                )
            ) {
                composable(Screen.Play.route) {
                    PlayScreen(mainViewModel = mainViewModel)
                }

                composable(Screen.Timer.route) {
                    TimerScreen(mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun TopGreenHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    )
}

@Composable
fun MainBottomBar(navigator: MainNavigator) {
    val navBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarItems = listOf(
        BottomNavItem(
            route = Screen.Play.route,
            title = "재생",
            iconRes = R.drawable.round_smart_display_24,
            onClick = navigator::navigateToPlay
        ),
        BottomNavItem(
            route = Screen.Timer.route,
            title = "타이머",
            iconRes = R.drawable.round_timer_24,
            onClick = navigator::navigateToTimer
        )
    )

    NavigationBar {
        bottomBarItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp),
                        tint = if (currentRoute == item.route)
                            Color(0xFF4DB6AC)
                        else
                            Color(0xFF9E9E9E)
                    )

                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = item.onClick,
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val iconRes: Int,
    val onClick: () -> Unit
)