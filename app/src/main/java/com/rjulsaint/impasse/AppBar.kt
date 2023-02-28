package com.rjulsaint.impasse

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBar {
    private val tag : String = "AppBar"

    @Composable
    fun TopBar(
        coroutineScope: CoroutineScope,
        scaffoldState: ScaffoldState,
        navHostController: NavHostController,
        sessionManager: SessionManager,
        databaseHelper: DatabaseHelper,
    ){
        val backStackEntry = navHostController.currentBackStackEntryAsState()
        var openDialog by remember { mutableStateOf(false) }
        TopAppBar(
            actions = {
                if(sessionManager.sessionUserName != null && sessionManager.sessionUserName != "" && (backStackEntry.value?.destination?.route == ScreenNavigation.ViewPasswords.route)){
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Rounded.Search, "Search Icon")
                    }
                }

                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "") && backStackEntry.value?.destination?.route != ScreenNavigation.Login.route && backStackEntry.value?.destination?.route != ScreenNavigation.NewUser.route) {
                    IconButton(
                        onClick = {
                            try {
                                navHostController.navigate(ScreenNavigation.Settings.route)
                            } catch (ex: IllegalArgumentException) {
                                Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                            }
                        }
                    ) {
                        Icon(Icons.Rounded.Settings, "Settings Icon")
                    }
                }

                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "") && backStackEntry.value?.destination?.route != ScreenNavigation.Login.route && backStackEntry.value?.destination?.route != ScreenNavigation.NewUser.route){
                    IconButton(
                       onClick = {
                           openDialog = true
                       }
                    ) {
                        Icon(Icons.Rounded.AccountCircle, "Account Icon")
                    }
                }
            },
            title = {
                Text(
                    text= if(backStackEntry.value?.destination?.route == null || backStackEntry.value?.destination?.route == ScreenNavigation.Login.route)"ImPasse" else backStackEntry.value?.destination?.route!!,
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            },
            navigationIcon = {
                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "") && backStackEntry.value?.destination?.route != ScreenNavigation.Login.route && backStackEntry.value?.destination?.route != ScreenNavigation.NewUser.route) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Rounded.Menu, "Hamburger Menu")
                    }
                }
            },
        )
        if(openDialog) {
            EditProfileDialogActivity().DisplayEditProfileDialogBox(
                onDismiss = { openDialog = false },
                databaseHelper = databaseHelper,
                sessionManager = sessionManager
            )
        }
    }
}