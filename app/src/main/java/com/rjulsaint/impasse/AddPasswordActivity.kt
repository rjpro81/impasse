package com.rjulsaint.impasse

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class AddPasswordActivity {
    @Composable
    private fun DisplayAddPasswordFields(databaseHelper: DatabaseHelper) {
        val focusManager = LocalFocusManager.current
        val readableDB = databaseHelper.readableDatabase
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
                var webAddress by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
                var passwordVisible by remember { mutableStateOf(false) }
                var errorOnSubmission by remember { mutableStateOf(false) }

                Text("Add Password", color = Color.Gray)
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = webAddress,
                    label = { Text("Web Address") },
                    onValueChange =
                    {
                        webAddress = it
                        textFieldInputIsError = false
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                OutlinedTextField(
                    value = description,
                    label = { Text("Site/Description") },
                    onValueChange =
                    {
                        description = it
                        textFieldInputIsError = false
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                OutlinedTextField(
                    value = password,
                    label = { Text("Password") },
                    onValueChange =
                    {
                        password = it
                        textFieldInputIsError = false
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if(passwordVisible){
                            painterResource(id = R.drawable.passwordvisibleicon)
                        } else {
                            painterResource(id = R.drawable.passwordnotvisibleicon)
                        }
                        val accessibilityDescription = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(image, accessibilityDescription, Modifier.size(30.dp))
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
                //This code will be used to determine if password has been submitted successfully
                if (errorOnSubmission) {
                    Text(
                        text = "There was an error on password submission please retry",
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
                            webAddress = ""
                            description = ""
                            password = ""
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(end = 2.dp)
                    ) {
                        Text(text = "Clear", color = Color.White)
                    }

                    Button(
                        onClick =
                        {
                            val masterPassword = LoginActivity().sessionMasterPassword
                            errorOnSubmission = databaseHelper.addNewPassword(writeableDB, webAddress,description,password, masterPassword)!! < 0
                            webAddress = ""
                            description = ""
                            password = ""
                            readableDB.close()
                            writeableDB.close()
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Text(text = "Add Password", color = Color.White)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                Button(
                    onClick =
                    {
                        val generatedPassword = PasswordUtility().generate()
                        password = generatedPassword
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                    elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .size(width = 200.dp, height = 36.dp)
                ) {
                    Text(text = "Generate", color = Color.White)
                }
            }
        }
    }

    @Composable
    fun DisplayAddPasswordScreen(databaseHelper: DatabaseHelper) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            val navController = rememberNavController()
            Scaffold(
                topBar = { AppBar().TopBar(coroutineScope = coroutineScope, scaffoldState = scaffoldState) },
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

                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Drawer().AppDrawer(coroutineScope = coroutineScope, scaffoldState = scaffoldState, navHostController = navController)
                    }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding))
                DisplayAddPasswordFields(databaseHelper)
            }
        }
    }
}
