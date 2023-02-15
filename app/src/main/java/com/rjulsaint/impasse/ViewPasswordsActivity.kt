package com.rjulsaint.impasse

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.Builder
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme


class ViewPasswordsActivity {
    private val tag : String = "ViewPasswordActivity"
    @Composable
    private fun DisplayViewPasswordFields(databaseHelper: DatabaseHelper){
        val alertDialogBuilder = Builder(LocalContext.current)
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val focusManager = LocalFocusManager.current
        val writeableDB = databaseHelper.writableDatabase
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Passwords", color = Color.Gray)
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                var passwordsList : MutableList<List<String>>? = null
                try {
                    passwordsList = databaseHelper.getAllUserStoredPasswords(
                        writeableDB,
                        LoginActivity().sessionMasterPassword
                    )
                    writeableDB.close()
                } catch(ex : Exception){
                    Log.e(tag, "Unable to access database to retrieve a list of all passwords.", ex)
                }
                passwordsList!!.forEach { password ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color.LightGray
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = password[0]
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = password[1]
                            )
                            var passwordVisible by remember { mutableStateOf(false) }

                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = if (passwordVisible) password[2] else "**********",
                                softWrap = true,
                                overflow = TextOverflow.Visible
                            )
                            val image = if(passwordVisible){
                                painterResource(id = R.drawable.baseline_visibility_24)
                            } else {
                                painterResource(id = R.drawable.baseline_visibility_off_24)
                            }
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            Row {
                                // Toggle button to hide or display password
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(image, description, Modifier.size(30.dp))
                                }
                                IconButton(onClick = { clipboardManager.setText(
                                    AnnotatedString(password[2])); Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show() }){
                                    Icon(painterResource(id = R.drawable.outline_content_copy_24), "Copy Contents", Modifier.size(30.dp))
                                }
                                IconButton(onClick = {
                                    onDeletePress(alertDialogBuilder, databaseHelper, password[0], password[1])
                                    clipboardManager.setText(
                                        AnnotatedString(password[2])); Toast.makeText(context, "Password deleted", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(painterResource(id = R.drawable.baseline_delete_24), "Delete Password", Modifier.size(30.dp))
                                }
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    fun ViewPasswordsScreen(navHostController: NavHostController, databaseHelper: DatabaseHelper, builder : Builder) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            Scaffold(
                topBar = { AppBar().TopBar(
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState,
                    databaseHelper = databaseHelper,
                    builder = builder,
                    navHostController = navHostController
                ) },
                scaffoldState = scaffoldState,
                drawerBackgroundColor = Color.DarkGray,
                drawerGesturesEnabled = true,
                drawerContent = {
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
                    DisplayViewPasswordFields(databaseHelper)
                }
            }
        }
    }

    private fun onDeletePress(builder : Builder, databaseHelper: DatabaseHelper, webAddress: String, description : String){
        builder.setMessage("Are you sure you want to delete password?")
        builder.setTitle("Alert!!")
        builder.setCancelable(false)
        try {
            builder.setPositiveButton("Yes") {
                // When the user click yes button then app will close
                    _, _ ->
                databaseHelper.deletePassword(databaseHelper.writableDatabase, webAddress, description)
            }
        } catch(ex : java.lang.Exception){
            Log.e(tag, "Unable to access the database to delete password.", ex)
        }

        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, _ -> dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
