package com.rjulsaint.impasse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class MainActivity : ComponentActivity() {
    private val tag : String = "MainActivity"
    private val sessionManager = SessionManager.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
               val databaseHelper = DatabaseHelper(this).getInstance()
                try {
                    //databaseHelper.onUpgrade(databaseHelper.writableDatabase, 1, 2)
                    databaseHelper.onCreate(databaseHelper.writableDatabase)
                } catch(ex : Exception){
                    Log.e(tag, "Unable to access database to perform onCreate function.", ex)
                }
                val navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph().NavGraph(
                        navHostController = navHostController,
                        databaseHelper = databaseHelper,
                        sessionManager = sessionManager
                    )
                }
            }
        }
    }
}