package com.rjulsaint.impasse

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class NavigationGraph {
    @Composable
    fun NavGraph(
        navHostController: NavHostController,
        databaseHelper: DatabaseHelper,
        sessionManager: SessionManager
    ){
        NavHost(navController = navHostController, startDestination = ScreenNavigation.Login.route){
            composable(route = ScreenNavigation.Login.route){
                LoginActivity().DisplayLoginScreen(navHostController, databaseHelper, sessionManager)
            }
            composable(route = ScreenNavigation.NewUser.route){
                NewUserActivity().DisplayUsernameScreen(navHostController, databaseHelper)
            }
            composable(route = ScreenNavigation.AddPassword.route){
                AddPasswordActivity().DisplayAddPasswordScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,sessionManager,
                )
            }
            composable(route = ScreenNavigation.ViewPasswords.route){
                ViewPasswordsActivity().ViewPasswordsScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,sessionManager,
                )
            }
            composable(ScreenNavigation.Settings.route){
                SettingsActivity().DisplaySettingsScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper
                )
            }
        }
    }
}