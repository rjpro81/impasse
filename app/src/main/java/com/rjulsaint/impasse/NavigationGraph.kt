package com.rjulsaint.impasse

import androidx.appcompat.app.AlertDialog.Builder
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class NavigationGraph {
    @Composable
    fun NavGraph(navHostController: NavHostController, databaseHelper: DatabaseHelper, builder : Builder){
        NavHost(navController = navHostController, startDestination = ScreenNavigation.Login.route){
            composable(route = ScreenNavigation.Login.route){
                LoginActivity().DisplayLoginScreen(navHostController, databaseHelper, builder)
            }
            composable(route = ScreenNavigation.NewUser.route){
                NewUserActivity().DisplayUsernameScreen(navHostController, databaseHelper, builder)
            }
            composable(route = ScreenNavigation.AddPassword.route){
                AddPasswordActivity().DisplayAddPasswordScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    builder = builder
                )
            }
            composable(route = ScreenNavigation.ViewPasswords.route){
                ViewPasswordsActivity().ViewPasswordsScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    builder = builder
                )
            }
        }
    }
}