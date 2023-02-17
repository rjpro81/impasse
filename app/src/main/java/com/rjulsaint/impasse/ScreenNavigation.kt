package com.rjulsaint.impasse

sealed class ScreenNavigation(val route: String) {
    object Login: ScreenNavigation("login_screen")
    object NewUser: ScreenNavigation("newUser_screen")
    object ViewPasswords: ScreenNavigation("viewPasswords_screen")
    object AddPassword: ScreenNavigation("addPasswords_screen")
    object Settings: ScreenNavigation("Settings")
}