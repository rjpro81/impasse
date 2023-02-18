package com.rjulsaint.impasse

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class LoginActivity {
    private val tag : String = "LoginActivity"
    @Composable
    private fun DisplayLoginFields(navHostController: NavHostController, databaseHelper: DatabaseHelper, sessionManager: SessionManager) {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
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
                var masterPassword by remember { mutableStateOf("") }
                var userName by remember { mutableStateOf("") }
                var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
                var passwordVisible by remember { mutableStateOf(false) }

                Text("Please login", color = Color.Gray)
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = userName,
                    label = { Text("Username") },
                    onValueChange =
                    {
                        userName = it
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                OutlinedTextField(
                    value = masterPassword,
                    label = { Text("Master Password") },
                    onValueChange =
                    {
                        masterPassword = it
                        textFieldInputIsError = false
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if(passwordVisible){
                            painterResource(id = R.drawable.baseline_visibility_24)
                        } else {
                            painterResource(id = R.drawable.baseline_visibility_off_24)
                        }
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(image, description, Modifier.size(30.dp))
                        }
                    }
                )

                if (textFieldInputIsError) {
                    Text(
                        text = "Wrong password",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                Row {
                    Button(
                        onClick =
                        {
                            try {
                                navHostController.navigate(ScreenNavigation.NewUser.route)
                            } catch (ex : IllegalArgumentException){
                                Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                            }
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(end = 2.dp)
                    ) {
                        Text(text = "New User", color = Color.White)
                    }

                    Button(
                        onClick =
                        {
                            var valid = false
                            try {
                                sessionManager.setUserName(userName)
                                sessionManager.setMasterPassword(masterPassword)

                                valid = databaseHelper.masterPasswordLogin(
                                    databaseHelper.writeableDB,
                                    sessionManager.sessionMasterPassword!!,
                                    sessionManager.sessionUserName!!
                                )
                            } catch(ex : Exception){
                                Log.e(tag, "Unable to access database to validate user profile.", ex)
                            }
                            if (valid) {
                                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                                try {
                                    navHostController.navigate(ScreenNavigation.AddPassword.route)
                                } catch (ex : IllegalArgumentException){
                                    Log.e(tag, "Unable to navigate to AddPasswordActivity screen.", ex)
                                }
                            } else {
                                textFieldInputIsError = true
                            }

                            userName = ""
                            masterPassword = ""
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Text(text = "Login", color = Color.White)
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayLoginScreen(navHostController: NavHostController, databaseHelper: DatabaseHelper, sessionManager: SessionManager) {
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
                    DisplayLoginFields(navHostController, databaseHelper, sessionManager)
                }
            }
        }
    }
}