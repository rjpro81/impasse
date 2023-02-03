package com.rjulsaint.impasse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
            Text(text = "Master Password:", color = Color.Blue, style = TextStyle(color = Color.Blue))
            var text = "Enter here"
            TextField(
                value = text,
                textStyle = TextStyle(color = Color.White),
                onValueChange = { text = it },
                enabled = true,
                shape = AbsoluteRoundedCornerShape(corner = CornerSize(15.dp)),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Blue)
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp)
            )
            Text(text = "Skip for now", color = Color.Cyan, style = TextStyle(color = Color.Cyan, textDecoration = TextDecoration.Underline))
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