package com.rjulsaint.impasse

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class NewUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val databaseHelper = DatabaseHelper(this)
                    databaseHelper.onCreate(databaseHelper.writableDatabase)
                    ImpassePreview(databaseHelper)
                }
            }
        }
    }

    @Composable
    private fun DisplayUserName(databaseHelper: DatabaseHelper) {
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
                var userName by remember { mutableStateOf("") }
                var masterPassword by remember { mutableStateOf("") }
                var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
                var passwordVisible by remember { mutableStateOf(false) }

                Text("New User", color = Color.Gray)
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
                        textFieldInputIsError = false
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
                            painterResource(id = R.drawable.passwordnotvisibleicon)
                        } else {
                            painterResource(id = R.drawable.passwordvisibleicon)
                        }
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(image, description, Modifier.size(30.dp))
                        }
                    }
                )
                //This code will be used to meet password requirements
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
                            val navigate = Intent(this@NewUserActivity, MainActivity::class.java)
                            startActivity(navigate)
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(end = 2.dp)
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }

                    Button(
                        onClick =
                        {
                            if (databaseHelper.addNewUser(writeableDB, userName, masterPassword)!! >= 0) {
                                val navigate = Intent(this@NewUserActivity, MainActivity::class.java)
                                startActivity(navigate)
                            } else {
                                textFieldInputIsError = true
                            }
                            userName = ""
                            masterPassword = ""
                            readableDB.close()
                            writeableDB.close()
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Text(text = "Create", color = Color.White)
                    }
                }
            }
        }
    }

    //@Preview(showBackground = true)
    @Composable
    private fun ImpassePreview(databaseHelper : DatabaseHelper) {
        ImPasseTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color.DarkGray
                    ) {
                        Text(
                            "ImPasse",
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding))
                DisplayUserName(databaseHelper)
            }
        }
    }
}