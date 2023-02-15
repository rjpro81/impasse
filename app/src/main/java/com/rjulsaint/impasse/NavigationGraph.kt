package com.rjulsaint.impasse

import androidx.appcompat.app.AlertDialog.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class NavigationGraph {
    @Composable
    fun NavGraph(navHostController: NavHostController, databaseHelper: DatabaseHelper){
        val alertDialogBuilder = Builder(LocalContext.current)
        NavHost(navController = navHostController, startDestination = ScreenNavigation.Login.route){
            composable(route = ScreenNavigation.Login.route){
                LoginActivity().DisplayLoginScreen(navHostController, databaseHelper, alertDialogBuilder)
            }
            composable(route = ScreenNavigation.NewUser.route){
                NewUserActivity().DisplayUsernameScreen(navHostController, databaseHelper, alertDialogBuilder)
            }
            composable(route = ScreenNavigation.AddPassword.route){
                AddPasswordActivity().DisplayAddPasswordScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    builder = alertDialogBuilder
                )
            }
            composable(route = ScreenNavigation.ViewPasswords.route){
                ViewPasswordsActivity().ViewPasswordsScreen(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    builder = alertDialogBuilder
                )
            }
        }
    }
}