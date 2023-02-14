package com.rjulsaint.impasse

import android.os.Bundle
//import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                val databaseHelper = DatabaseHelper(this)
                val alertDialogBuilder = AlertDialog.Builder(this)
                databaseHelper.onCreate(databaseHelper.writableDatabase)
                val navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph().NavGraph(
                        navHostController = navHostController,
                        databaseHelper = databaseHelper,
                        builder = alertDialogBuilder
                    )
                }
            }
        }
    }
}