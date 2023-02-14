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
                        onDeletePress(builder, databaseHelper)
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

    private fun onDeletePress(builder : Builder, databaseHelper: DatabaseHelper){
        builder.setMessage("Are you sure you want to delete all passwords?")
        builder.setTitle("Alert!!")
        builder.setCancelable(false)
        try {
            builder.setPositiveButton("Yes") {
                // When the user click yes button then app will close
                _, _ ->
                databaseHelper.deleteAllPasswords(databaseHelper.writableDatabase)
            }
        } catch(ex : java.lang.Exception){
            Log.e(tag, "Unable to access the dagabase to delete all records.", ex)
        }

        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, _ -> dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}