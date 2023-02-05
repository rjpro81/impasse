package com.rjulsaint.impasse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
//import android.database.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ImpassePreview()
                }
            }
        }
    }
}



@Composable
fun DisplayMasterPassword() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            var text by remember { mutableStateOf("") }
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
            Text(text = "Skip for now", color = Color.Cyan, style = TextStyle(color = Color.Cyan, textDecoration = TextDecoration.Underline))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 2.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(onClick = { /**/ }) {
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImpassePreview() {
    ImPasseTheme {
        Scaffold(
            topBar = {
                TopAppBar (
                      backgroundColor = Color.Blue
                        ){
                    Text(
                        "Impasse",
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding))
            DisplayMasterPassword()
        }
    }
}