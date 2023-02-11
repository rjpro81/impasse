package com.rjulsaint.impasse

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class NavigationGraph {
    @Composable
    fun NavGraph(navHostController: NavHostController, databaseHelper: DatabaseHelper){
        NavHost(navController = navHostController, startDestination = ScreenNavigation.Login.route){
            composable(route = ScreenNavigation.Login.route){
                LoginActivity().DisplayLoginScreen(navHostController, databaseHelper)
            }
            composable(route = ScreenNavigation.NewUser.route){
                NewUserActivity().DisplayUsernameScreen(navHostController, databaseHelper)
            }
            composable(route = ScreenNavigation.AddPassword.route){
                AddPasswordActivity().DisplayAddPasswordScreen(
                    databaseHelper = databaseHelper
                )
            }
            composable(route = ScreenNavigation.ViewPasswords.route){
                PasswordManagement().DisplayPasswordManagementScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper
                )
            }
        }
    }
}