package com.rjulsaint.impasse

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.FragmentActivity

class EditProfileDialogActivity : FragmentActivity(){
    private val tag: String = "ViewPasswordDialogActivity"
    @Composable
    fun DisplayEditProfileDialogBox(onDismiss: () -> Unit, sessionManager: SessionManager) {
        var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        val errorOnSubmission by remember { mutableStateOf(false) }
        val isExistingPassword by rememberSaveable { mutableStateOf(false) }
        var userName by remember { mutableStateOf(sessionManager.sessionUserName!!) }
        var masterPassword by remember { mutableStateOf(sessionManager.sessionMasterPassword!!) }
        val context = LocalContext.current

        Dialog(
            onDismissRequest = {
                onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(color = Color(0xFF35898f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Profile", color = Color.White)
                    }

                    Spacer(modifier = Modifier.padding(top = 5.dp))

                    OutlinedTextField(
                        value = userName,
                        label = { Text("UserName") },
                        onValueChange =
                        {
                            userName = it
                        },
                        enabled = true,
                        readOnly = true,
                        shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                        isError = textFieldInputIsError || errorOnSubmission || isExistingPassword,
                    )

                    OutlinedTextField(
                        value = masterPassword,
                        label = { Text("Master Password") },
                        onValueChange =
                        {
                            if (masterPassword.length <= 60) masterPassword = it
                            textFieldInputIsError = false
                        },
                        singleLine = true,
                        enabled = true,
                        readOnly = true,
                        shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) {
                                painterResource(id = R.drawable.baseline_visibility_24)
                            } else {
                                painterResource(id = R.drawable.baseline_visibility_off_24)
                            }
                            val accessibilityDescription =
                                if (passwordVisible) "Hide password" else "Show password"

                            // Toggle button to hide or display password
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, accessibilityDescription, Modifier.size(30.dp))
                            }
                        },
                        isError = textFieldInputIsError || errorOnSubmission || isExistingPassword
                    )
                    Spacer(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            try {
                                /*val result = databaseHelper.updateUserProfile(
                                    databaseHelper.writeableDB,
                                    sessionManager.sessionUserName!!,
                                    sessionManager.sessionMasterPassword!!,
                                    userName,
                                    masterPassword,
                                )*/
                               /* if (result > 0) {
                                    Toast.makeText(
                                        context,
                                        "Profile updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }*/
                                onDismiss()
                            } catch (ex: Exception) {
                                Log.e(tag, "Unable to access database to update profile")
                            }
                        }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.baseline_save_24),
                                "Save Profile",
                                Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}