package com.rjulsaint.impasse

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
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
        navHostController: NavHostController
    ){
        TopAppBar(
            actions = {
                IconButton(
                    onClick = {
                        try {
                            navHostController.navigate(ScreenNavigation.Settings.route)
                        } catch (ex : IllegalArgumentException){
                            Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                        }
                    }
                ){
                    Icon(Icons.Rounded.Settings, "Settings Icon")
                }
            },
            title = {
                Text(
                    text= "ImPasse",
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    val backStackEntry = navHostController.currentBackStackEntryAsState()

                    if(backStackEntry.value?.destination?.route != ScreenNavigation.Login.route) {
                        Icon(Icons.Rounded.Menu, "Hamburger Menu")
                    }
                }
            },
        )
    }
}