package com.rjulsaint.impasse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rjulsaint.impasse.ui.theme.ImPasseTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val databaseHelper = DatabaseHelper(this)
                    databaseHelper.onCreate(databaseHelper.writableDB)
                    ImpassePreview(databaseHelper)
                }
            }
        }
    }
}
@Composable
fun DisplayMasterPassword(databaseHelper: DatabaseHelper) {
    val focusManager = LocalFocusManager.current
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
            var text by remember { mutableStateOf("") }
            Text("Please login", color = Color.Gray)
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = text,
                label = { Text("Master Password") },
                onValueChange = { text = it },
                singleLine = true,
                enabled = true,
                shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            Button(
                onClick = { databaseHelper.masterPasswordLogin(databaseHelper.readableDB, text) },
                enabled = true,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                elevation = ButtonDefaults.elevation(pressedElevation = 5.dp)
            ) {
                Text(text = "Login", color = Color.White)
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun ImpassePreview(databaseHelper : DatabaseHelper) {
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
            DisplayMasterPassword(databaseHelper)
        }
    }
}
