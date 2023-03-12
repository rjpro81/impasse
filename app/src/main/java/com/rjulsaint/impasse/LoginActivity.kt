package com.rjulsaint.impasse

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
import java.util.concurrent.Executor


class LoginActivity() : AppCompatActivity() {
    private val tag : String = "LoginActivity"
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val sessionManager = SessionManager.instance
    var databaseHelper :DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    databaseHelper = DatabaseHelper(this)
                    DisplayLoginScreen(databaseHelper!!)
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun DisplayLoginFields(databaseHelper: DatabaseHelper) {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val coroutineScope = rememberCoroutineScope()
        val navHostController = rememberNavController()
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current as FragmentActivity

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
                var biometricAuthentication by remember { mutableStateOf(false) }

                val autoFillNode = AutofillNode(
                    autofillTypes = listOf(AutofillType.Username),
                    onFill = { userName = it }
                )
                val autofill = LocalAutofill.current
                LocalAutofillTree.current += autoFillNode

                Text("Please login", color = Color.Gray)
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .onGloballyPositioned {
                            autoFillNode.boundingBox = it.boundsInWindow()
                        }
                        .onFocusChanged { focusState ->
                            autofill?.run {
                                if (focusState.isFocused) {
                                    requestAutofillForNode(autoFillNode)
                                } else {
                                    cancelAutofillForNode(autoFillNode)
                                }
                            }
                        }
                )
                OutlinedTextField(
                    value = userName,
                    label = { Text("Username") },
                    onValueChange =
                    {
                        if(userName.length <= 60) userName = it
                    },
                    singleLine = true,
                    enabled = true,
                    shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                    visualTransformation = if (biometricAuthentication) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(autoCorrect = true),
                )
                OutlinedTextField(
                    value = masterPassword,
                    label = { Text("Master Password") },
                    onValueChange =
                    {
                        if(masterPassword.length <= 60) masterPassword = it
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
                    },
                    isError = textFieldInputIsError
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
                                val myIntent = Intent(this@LoginActivity, NewAccountActivity::class.java)
                                this@LoginActivity.startActivity(myIntent)
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
                            sessionManager.setUserNameForSession(userName)
                            sessionManager.setMasterPasswordForSession(masterPassword)
                            val writableDatabase: SQLiteDatabase = databaseHelper!!.writableDatabase
                            try {
                                valid = databaseHelper.masterPasswordLogin(
                                    writableDatabase,
                                    sessionManager.sessionMasterPassword!!,
                                    sessionManager.sessionUserName!!
                                )
                            } catch (ex: Exception) {
                                Log.e(
                                    tag,
                                    "Unable to access database to validate user profile.",
                                    ex
                                )
                            }

                            if (valid) {
                                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                                try {
                                    val myIntent = Intent(this@LoginActivity, AddPasswordActivity::class.java)
                                    this@LoginActivity.startActivity(myIntent)
                                } catch (ex : IllegalArgumentException){
                                    Log.e(tag, "Unable to navigate to AddPasswordActivity screen.", ex)
                                }
                            } else {
                                textFieldInputIsError = true
                                masterPassword = ""
                            }
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        elevation = ButtonDefaults.elevation(pressedElevation = 5.dp),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Text(text = "Login", color = Color.White)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )


                executor = ContextCompat.getMainExecutor(context)
                biometricPrompt = BiometricPrompt(context, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int,
                                                           errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(context,
                                "Authentication error: $errString", Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            biometricAuthentication = true
                            var user : MutableList<List<String>>? = null
                            try{
                                //user = databaseHelper.getImpasseUser(databaseHelper.writeableDB)
                            } catch(ex: Exception){
                                Log.e(
                                    tag,
                                    "Unable to access database to retrieve user.",
                                    ex
                                )
                            }
                            user?.forEach { user ->
                                userName = user[0].toString()
                                masterPassword = user[1].toString()
                            }
                            sessionManager.setUserNameForSession(userName)
                            sessionManager.setMasterPasswordForSession(masterPassword)
                            Toast.makeText(context,
                                "Authentication succeeded!", Toast.LENGTH_SHORT)
                                .show()

                            try {
                                navHostController.navigate(ScreenNavigation.AddPassword.route)
                            } catch (ex : IllegalArgumentException){
                                Log.e(tag, "Unable to navigate to AddPasswordActivity screen.", ex)
                            }
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(context, "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for Impasse")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build()

                IconButton(onClick = {
                    biometricPrompt.authenticate(promptInfo)
                }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_fingerprint_24), contentDescription = "Fingerprint button for biometric authentication")
                }
            }
        }
    }

    @Composable
    fun DisplayLoginScreen(databaseHelper: DatabaseHelper) {
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val navHostController = rememberNavController()
        ImPasseTheme {
            Scaffold(
                topBar = { AppBar(scaffoldState, coroutineScope).TopBar() },
                scaffoldState = scaffoldState,
                drawerBackgroundColor = Color.DarkGray,
                drawerGesturesEnabled = true,
                drawerContent = {
                    if(navHostController.currentBackStackEntry?.destination?.route != "Login")
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
                            Drawer().AppDrawer(
                                coroutineScope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplayLoginFields(databaseHelper)
                }
            }
        }
    }
}