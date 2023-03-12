package com.rjulsaint.impasse

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBar(private val scaffoldState: ScaffoldState, private val coroutineScope: CoroutineScope) {
    private val tag : String = "AppBar"
    private val sessionManager = SessionManager.instance

    @Composable
    fun TopBar() {
        val context = LocalContext.current
        var openDialog by remember { mutableStateOf(false) }
        TopAppBar(
            actions = {
                // include condition for view passwords screen
                if(sessionManager.sessionUserName != null && sessionManager.sessionUserName != ""){
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Rounded.Search, "Search Icon")
                    }
                }
                // include condition for not login or new account screen
                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "")) {
                    println(sessionManager.sessionUserName)
                    println(sessionManager.sessionUserName)
                    IconButton(
                        onClick = {
                            try {
                                val myIntent = Intent(context, SettingsActivity::class.java)
                                context.startActivity(myIntent)
                            } catch (ex: IllegalArgumentException) {
                                Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                            }
                        }
                    ) {
                        Icon(Icons.Rounded.Settings, "Settings Icon")
                    }
                }
                // include condition for not login or new account screen
                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "")){
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
                    text= "Impasse",
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            },
            // include condition for not login and new account screens
            navigationIcon = {
                if((sessionManager.sessionUserName != null && sessionManager.sessionUserName != "")) {
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
                sessionManager = sessionManager
            )
        }
    }
}