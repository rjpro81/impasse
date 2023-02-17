package com.rjulsaint.impasse

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class SettingsActivity {
    //private val tag : String = "SettingsActivity"
    @Composable
    private fun DisplaySettingsFields(/*navHostController: NavHostController, databaseHelper: DatabaseHelper*/){
        //val context = LocalContext.current

        val focusManager = LocalFocusManager.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            /*Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

            }*/
            Button(
                onClick =
                {

                },
                enabled = true,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                modifier = Modifier
                    .padding(end = 2.dp)
                    .size(width = 200.dp, height = 36.dp)
            ) {
                Text(text = "Reset Database", color = Color.White)
            }
        }
    }

    @Composable
    fun DisplaySettingsScreen(navHostController: NavHostController, databaseHelper: DatabaseHelper) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            Scaffold(
                topBar = { AppBar().TopBar(
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState,
                    databaseHelper = databaseHelper,
                    navHostController = navHostController
                ) },
                scaffoldState = scaffoldState,
                drawerBackgroundColor = Color.DarkGray,
                drawerGesturesEnabled = true,
                drawerContent = {
                    if(navHostController.currentBackStackEntry?.destination?.route != "login_screen")
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(126.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    modifier = Modifier
                                        .matchParentSize(),
                                    painter = painterResource(id = R.drawable.ic_launcher_background),
                                    contentDescription = "",
                                )

                                Image(
                                    modifier = Modifier
                                        .scale(1.4f),
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "",
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Drawer().AppDrawer(coroutineScope = coroutineScope, scaffoldState = scaffoldState, navHostController = navHostController)
                        }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplaySettingsFields(/*navHostController, databaseHelper*/)
                }
            }
        }
    }
}