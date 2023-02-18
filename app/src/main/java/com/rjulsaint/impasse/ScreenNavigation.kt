package com.rjulsaint.impasse

sealed class ScreenNavigation(val route: String) {
    object Login: ScreenNavigation("Login")
    object NewUser: ScreenNavigation("NewUser")
    object ViewPasswords: ScreenNavigation("Passwords")
    object AddPassword: ScreenNavigation("Home")
    object Settings: ScreenNavigation("Settings")
}