package com.example.whitenoiseapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberMainNavigator(
    navHostController: NavHostController = rememberNavController()
): MainNavigator = remember(navHostController) {
    MainNavigatorImpl(navHostController)
}

@Stable
class MainNavigatorImpl(
    override val navController: NavHostController
) : MainNavigator {
    override val startDestination: String
        get() = Screen.Play.route

    override fun navigateToPlay() {
        navigateWithBottomBarState(Screen.Play.route)
    }

    override fun navigateToTimer() {
        navigateWithBottomBarState(Screen.Timer.route)
    }

    private fun navigateWithBottomBarState(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}


interface MainNavigator {
    val startDestination: String
    val navController: NavHostController

    fun navigateToPlay()
    fun navigateToTimer()
}

sealed class Screen(val route: String) {
    data object Play : Screen("play")
    data object Timer : Screen("timer")
}