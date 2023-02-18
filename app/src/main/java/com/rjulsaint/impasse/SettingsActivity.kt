package com.rjulsaint.impasse

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class SettingsActivity {
    private val tag : String = "SettingsActivity"
    @Composable
    private fun DisplaySettingsFields(navHostController: NavHostController, databaseHelper: DatabaseHelper){
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .clickable { focusManager.clearFocus() }
                .fillMaxSize(),
        ) {
            val openDialog = remember { mutableStateOf(false) }
            val dismissAlertDialog = remember { mutableStateOf(true) }
            val eventName = remember { mutableStateOf("Reset Database") }
            if(openDialog.value) {
                AlertDialog(
                    onDismissRequest = { dismissAlertDialog.value },
                    title = {
                        Text(text = "Alert!!")
                    },
                    text = {
                        Text(text = if(eventName.value == "Reset Database") "Are you sure you want to reset database?\nThis will delete all saved application data" else "Are you sure you want to delete all passwords?\nThis operation cannot be undone")
                    },
                    buttons = {
                        Row(
                            modifier = Modifier
                                .padding(all = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    try {
                                        var numOfRecordsDeleted = -1
                                        if(eventName.value == "Reset Database") { databaseHelper.onUpgrade(databaseHelper.writeableDB, 1, 2) } else { numOfRecordsDeleted = databaseHelper.deleteAllPasswords(databaseHelper.writeableDB) }
                                        Toast.makeText(
                                            context,
                                            if(eventName.value == "Reset Database"){ "Database has been reset" } else { if (numOfRecordsDeleted > 0){ "Passwords deleted" } else { null } },
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        if(eventName.value == "Reset Database") navHostController.navigate(ScreenNavigation.Login.route)
                                    } catch (ex: Exception) {
                                        Log.e(
                                            tag,
                                            if (eventName.value == "Reset Database") "Unable to access database to reset or update database" else "Unable to access database to delete passwords",
                                            ex
                                        )
                                    }
                                }
                            ) {
                                Text("Yes")
                            }
                            Spacer(modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Application Settings", color = Color.Black)
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            Row {
                Icon(painter = painterResource(id = R.drawable.baseline_dataset_24), contentDescription = "Reset Database", modifier = Modifier.size(50.dp).padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        openDialog.value = true
                        eventName.value = "Reset Database"
                    },
                    text = AnnotatedString(text = "Reset Database"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
            }

            Row{
                Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = "Edit Users", modifier = Modifier.size(50.dp).padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        //openDialog.value = true
                    },
                    text = AnnotatedString(text = "Edit Users"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
            }

            Row{
                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "Delete Password", modifier = Modifier.size(50.dp).padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        openDialog.value = true
                        eventName.value = "Delete Passwords"
                    },
                    text = AnnotatedString(text = "Delete Passwords"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
            }

            Row{
                Icon(painter = painterResource(id = R.drawable.baseline_palette_24), contentDescription = "Themes", modifier = Modifier.size(50.dp).padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        //openDialog.value = true
                    },
                    text = AnnotatedString(text = "Themes"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
            }

            Row{
                Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = "Logout", modifier = Modifier.size(50.dp).padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        //openDialog.value = true
                    },
                    text = AnnotatedString(text = "Logout"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
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
                    DisplaySettingsFields(navHostController, databaseHelper)
                }
            }
        }
    }
}