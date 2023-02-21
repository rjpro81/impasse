package com.rjulsaint.impasse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties

class EditPasswordActivity {
    @Composable
    fun EditPasswordDialogBox(onDismiss: () -> Unit, selectedCategory: String, selectedUserName: String, selectedPassword: String, databaseHelper: DatabaseHelper, sessionManager: SessionManager){
        var mExpanded by remember { mutableStateOf(false) }
        var mSelectedText by remember { mutableStateOf("") }
        var mTextFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
        var category by remember { mutableStateOf(selectedCategory) }
        var userName by remember { mutableStateOf(selectedUserName) }
        var password by remember { mutableStateOf(selectedPassword) }
        var textFieldInputIsError by rememberSaveable { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        val errorOnSubmission by remember { mutableStateOf(false) }
        val isExistingPassword by rememberSaveable { mutableStateOf(false) }

        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ){
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp
            ){
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
                    ){
                        Text(text="Edit Password", color = Color.White)
                    }

                    Spacer(modifier = Modifier.padding(top = 5.dp))

                    val icon = if (mExpanded)
                        Icons.Filled.KeyboardArrowUp
                    else
                        Icons.Filled.KeyboardArrowDown

                    OutlinedTextField(
                        value = category,
                        label = { Text("Category") },
                        onValueChange =
                        {
                            category = it
                        },
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                mTextFieldSize = coordinates.size.toSize()
                            },
                        enabled = true,
                        shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                        isError = textFieldInputIsError || errorOnSubmission || isExistingPassword,
                        trailingIcon = {
                            Icon(icon,"contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded })
                        }
                    )

                    Box (
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                    ){
                        DropdownMenu(
                            expanded = mExpanded,
                            onDismissRequest = { mExpanded = false },
                            properties = PopupProperties(focusable = true),
                            modifier = Modifier
                                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                        ) {
                            Categories(databaseHelper, sessionManager.sessionUserName!!).getUserCategories().forEach { category ->
                                DropdownMenuItem(onClick = {
                                    mSelectedText = category
                                    mExpanded = false
                                }) {
                                    Text(text = category)
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = userName,
                        label = { Text("Username/Description") },
                        onValueChange =
                        {
                            if(userName.length <= 60) userName = it
                            textFieldInputIsError = false
                        },
                        singleLine = true,
                        enabled = true,
                        shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                        keyboardOptions = KeyboardOptions(autoCorrect = true),
                        isError = textFieldInputIsError || errorOnSubmission || isExistingPassword
                    )

                    OutlinedTextField(
                        value = password,
                        label = { Text("Password") },
                        onValueChange =
                        {
                            if(password.length <= 60) password = it
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
                            val accessibilityDescription = if (passwordVisible) "Hide password" else "Show password"

                            // Toggle button to hide or display password
                            IconButton(onClick = {passwordVisible = !passwordVisible}){
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
                        ClickableText(
                            text = AnnotatedString(text = "Delete"),
                            style = TextStyle(color = Color.Red,
                                fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            onClick = {},
                            modifier = Modifier.padding(end = 20.dp, bottom = 10.dp)
                        )
                        Spacer(modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                        ClickableText(
                            text = AnnotatedString(text = "Save"),
                            style = TextStyle(color = Color.Green,
                                fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            onClick = {},
                            modifier = Modifier.padding(end = 20.dp, bottom = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
