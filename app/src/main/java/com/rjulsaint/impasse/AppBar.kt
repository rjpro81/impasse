package com.rjulsaint.impasse

import android.content.res.Resources.NotFoundException
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.SQLException

class AppBar {
    private val tag : String = "AppBar"

    @Composable
    fun TopBar(
        coroutineScope: CoroutineScope,
        scaffoldState: ScaffoldState,
        databaseHelper: DatabaseHelper,
        navHostController: NavHostController
    ){
        val context = LocalContext.current
        TopAppBar(
            actions = {
                IconButton(onClick = {
                    try {
                        val numOfRecordsDeleted = databaseHelper.deleteAllPasswords(databaseHelper.writeableDB)
                        if((numOfRecordsDeleted > 0) && (navHostController.currentBackStackEntry?.destination?.route != "viewPasswords_screen")){
                            Toast.makeText(context, "Passwords deleted", Toast.LENGTH_SHORT).show()
                        } else if ((numOfRecordsDeleted > 0) && (navHostController.currentBackStackEntry?.destination?.route == "viewPasswords_screen")){
                            Toast.makeText(context, "Passwords deleted", Toast.LENGTH_SHORT).show()
                            navHostController.navigate(ScreenNavigation.ViewPasswords.route)
                        }
                    } catch(ex : SQLException){
                        Log.e(tag, "Unable to display alert dialog.", ex)
                    } catch(ex : NotFoundException){
                        Log.e(tag, "Unable to locate resource for displaying toast.", ex)
                    } catch(ex : IllegalArgumentException){
                        Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                    }
                },enabled = (navHostController.currentBackStackEntry?.destination?.route != "login_screen") && (navHostController.currentBackStackEntry?.destination?.route != "newUser_screen")){
                    Icon(Icons.Rounded.Delete, "Delete Icon")
                }

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
                    Icon(Icons.Rounded.Menu, "Drawer Icon")
                }
            },
        )
    }
}