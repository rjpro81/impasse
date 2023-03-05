package com.rjulsaint.impasse

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope

class NavigationGraph {
    @Composable
    fun NavGraph(
        navHostController: NavHostController,
        databaseHelper: DatabaseHelper,
        sessionManager: SessionManager,
        coroutineScope: CoroutineScope,
        scaffoldState: ScaffoldState
    ){
        NavHost(navController = navHostController, startDestination = ScreenNavigation.Login.route){
            composable(route = ScreenNavigation.Login.route){
                LoginActivity(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    sessionManager = sessionManager,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                ).DisplayLoginScreen()
            }
            composable(route = ScreenNavigation.NewUser.route){
                NewAccountActivity(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    sessionManager = sessionManager,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                ).DisplayUsernameScreen()
            }
            composable(route = ScreenNavigation.AddPassword.route){
                AddPasswordActivity(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    sessionManager = sessionManager,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                ).DisplayAddPasswordScreen()
            }
            composable(route = ScreenNavigation.ViewPasswords.route){
                ViewPasswordsActivity(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    sessionManager = sessionManager,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                ).ViewPasswordsScreen()
            }
            composable(ScreenNavigation.Settings.route){
                SettingsActivity(
                    navHostController = navHostController,
                    databaseHelper = databaseHelper,
                    sessionManager = sessionManager,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                ).DisplaySettingsScreen()
            }
        }
    }
}