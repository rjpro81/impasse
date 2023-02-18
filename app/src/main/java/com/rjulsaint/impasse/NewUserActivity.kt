package com.rjulsaint.impasse

import android.content.res.Resources.NotFoundException
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

class NewUserActivity {
    private val tag : String = "NewUserActivity"
    @Composable
    private fun DisplayUsernameFields(navHostController: NavHostController, databaseHelper: DatabaseHelper) {
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
                var userName by remember { mutableStateOf("") }
                var masterPassword by remember { mutableStateOf("") }
                var confirmingPassword by remember { mutableStateOf("") }
                var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
                var isExistingUser by rememberSaveable { mutableStateOf(false) }
                var passwordVisible by remember { mutableStateOf(false) }
                var confirmPasswordVisible by remember { mutableStateOf(false) }
                var matchingPassword by remember { mutableStateOf(true) }
                var hasUserName by remember { mutableStateOf(true) }

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
                OutlinedTextField(
                    value = confirmingPassword,
                    label = { Text("Confirm Password") },
                    onValueChange =
                    {
                        confirmingPassword = it
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if(confirmPasswordVisible){
                            painterResource(id = R.drawable.baseline_visibility_24)
                        } else {
                            painterResource(id = R.drawable.baseline_visibility_off_24)
                        }
                        val description = if (confirmPasswordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = {confirmPasswordVisible = !confirmPasswordVisible}){
                            Icon(image, description, Modifier.size(30.dp))
                        }
                    }
                )
                //This code will be used to meet username requirements
                if (!hasUserName) {
                    Text(
                        text = "username must be a minimum of 8 characters",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                //This code will be used to meet password requirements
                if (textFieldInputIsError) {
                    Text(
                        text = "1 uppercase, 1 lowercase letter, 1 special character,\nand minimum 8 characters is required",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                //This code will be used to determine of passwords match
                if (!matchingPassword) {
                    Text(
                        text = "The passwords do not match",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                //This code will be used to validate new user creation
                if(isExistingUser){
                    Text(
                        text = "This user already exists",
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
                                navHostController.navigate(ScreenNavigation.Login.route)
                            } catch(ex : Exception){
                                Log.e(tag, "Unable to navigate to Login screen.", ex)
                            }
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
                            var result : Long? = null
                            var index : Int = -1
                            try {
                                val users : MutableList<List<String>> = databaseHelper.getAllUsers(databaseHelper.writeableDB, userName, masterPassword)
                                users.forEach{ user ->
                                    index = user.binarySearch(userName)
                                }
                                result = databaseHelper.addNewUser(databaseHelper.writeableDB, userName, masterPassword)
                            } catch (ex : Exception){
                                Log.e(tag, "Unable to access database to add new user.", ex)
                            }

                            if(!PasswordUtility().validate(masterPassword)){
                                textFieldInputIsError = true
                            } else if (userName.length < 8){
                                hasUserName = false
                            } else if(masterPassword != confirmingPassword) {
                                matchingPassword = false
                            } else if (index >= 0){
                                isExistingUser = true
                            } else if (result!! >= 0 && masterPassword != "") {
                                try {
                                    Toast.makeText(context, "New user created", Toast.LENGTH_SHORT)
                                        .show()
                                    navHostController.navigate(ScreenNavigation.Login.route)
                                }catch(ex : NotFoundException){
                                    Log.e(tag, "Unable to locate resource for displaying toast.", ex)
                                } catch(ex : IllegalArgumentException){
                                    Log.e(tag, "Unable to navigate due to invalid route given.", ex)
                                }
                            }

                            userName = ""
                            masterPassword = ""
                            confirmingPassword = ""
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
    fun DisplayUsernameScreen(navHostController: NavHostController, databaseHelper: DatabaseHelper) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            //val navHostController = rememberNavController()
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
                    if(navHostController.currentBackStackEntry?.destination?.route != "newUser_screen")
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
                Box(modifier = Modifier.padding(contentPadding))
                DisplayUsernameFields(navHostController, databaseHelper)
            }
        }
    }
}