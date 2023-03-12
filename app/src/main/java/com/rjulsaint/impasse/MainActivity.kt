package com.rjulsaint.impasse

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme

class MainActivity : AppCompatActivity() {
    private val tag : String = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImPasseTheme {
                try {
                    val databaseHelper = DatabaseHelper(this)
                    val writableDatabase: SQLiteDatabase = databaseHelper.writableDatabase
                    //databaseHelper.onUpgrade(databaseHelper.writableDatabase, 1, 2)
                    databaseHelper.onCreate(writableDatabase)
                } catch(ex : Exception){
                    Log.e(tag, "Unable to access database to perform onCreate function.", ex)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val myIntent = Intent(this@MainActivity, LoginActivity::class.java)
                    this.startActivity(myIntent)
                }
            }
        }
    }
}