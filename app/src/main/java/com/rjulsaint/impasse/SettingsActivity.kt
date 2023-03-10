package com.rjulsaint.impasse

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class SettingsActivity() : AppCompatActivity(){
    private val tag : String = "SettingsActivity"
    private var databaseHelper : DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    databaseHelper = DatabaseHelper(this)
                    DisplaySettingsScreen(databaseHelper!!)
                }
            }
        }
    }
    @Composable
    private fun DisplaySettingsFields(databaseHelper: DatabaseHelper) {
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val sessionManager = SessionManager.instance
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val writableDatabase: SQLiteDatabase = databaseHelper.writableDatabase
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
                                        val numOfRecordsDeleted = databaseHelper.deleteAllPasswords(writableDatabase)
                                        if(numOfRecordsDeleted > 0) {
                                            Toast.makeText(
                                                context,
                                                "Passwords deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        openDialog.value = false
                                    } catch (ex: Exception) {
                                        Log.e(
                                            tag,
                                            "Unable to access database to delete passwords",
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

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = "Edit Users", modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        //openDialog.value = true
                    },
                    text = AnnotatedString(text = "Edit Profile"),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "Delete Password", modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp, end = 10.dp))

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(id = R.drawable.baseline_palette_24), contentDescription = "Themes", modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp, end = 10.dp))

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = "Logout", modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp, end = 10.dp))

                ClickableText(
                    onClick = {
                        val myIntent = Intent(this@SettingsActivity, LoginActivity::class.java)
                        this@SettingsActivity.startActivity(myIntent)
                        sessionManager.sessionUserName = null
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
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
    fun DisplaySettingsScreen(databaseHelper: DatabaseHelper) {
        ImPasseTheme {
            val sessionManager = SessionManager.instance
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            Scaffold(
                topBar = { AppBar(scaffoldState, coroutineScope).TopBar() },
                scaffoldState = scaffoldState,
                drawerBackgroundColor = Color.DarkGray,
                drawerGesturesEnabled = true,
                drawerContent = {
                    if(LocalContext.current != this@SettingsActivity)
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
                            Text(text = sessionManager.sessionUserName!!, color = Color.Magenta)

                            Drawer().AppDrawer(
                                coroutineScope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplaySettingsFields(databaseHelper)
                }
            }
        }
    }
}