package com.rjulsaint.impasse

import android.util.Log
import androidx.appcompat.app.AlertDialog.Builder
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBar {
    private val tag : String = "AppBar"
    @Composable
    fun TopBar(
        coroutineScope: CoroutineScope,
        scaffoldState: ScaffoldState,
        databaseHelper: DatabaseHelper,
        builder: Builder,
        navHostController: NavHostController
    ){
        TopAppBar(
            actions = {
                IconButton(onClick = {
                    try {
                        databaseHelper.onDeletePress(databaseHelper, builder, true,"Are you sure you want to delete all passwords?", "Unable to access database to delete passwords.")
                    } catch(ex : Exception){
                        Log.e(tag, "Unable to display alert dialog.", ex)
                    }
                },enabled = (navHostController.currentBackStackEntry?.destination?.route != "login_screen") && (navHostController.currentBackStackEntry?.destination?.route != "newUser_screen")){
                    Icon(Icons.Rounded.Delete, "Delete Icon")
                }

                IconButton(onClick = {}){
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
                    Icon(Icons.Rounded.Menu, "Drawer Icon")
                }
            },
        )
    }
}