package com.rjulsaint.impasse

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable

class Categories(private val userName: String) : AppCompatActivity(){
    private val staticCategories = listOf("Financial", "School", "Health", "Shopping", "Games", "Travel", "Websites", "Social Media", "Misc")
    private var userCreatedCategories :MutableList<String> = ArrayList()
    private val mergedCategoriesList: MutableList<String> = ArrayList()
    private val tag: String = "Categories"
    val databaseHelper = DatabaseHelper(this)
    private val writableDatabase: SQLiteDatabase = databaseHelper.writableDatabase

    @Composable
    /*
    fun DisplayCategories(){
        val context = LocalContext.current
    }*/

    fun getUserCategories(): List<String>{
        try {
            userCreatedCategories =
                databaseHelper.getAllCategories(writableDatabase, userName)
        } catch (ex: Exception){
            Log.e(tag, "Unable to access database to retrieve all categories.", ex)
        }

        mergedCategoriesList.addAll(staticCategories)
        mergedCategoriesList.addAll(userCreatedCategories)

        return mergedCategoriesList
    }
}