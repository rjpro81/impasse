package com.rjulsaint.impasse

import android.util.Log
import androidx.compose.runtime.Composable

class Categories(private val databaseHelper: DatabaseHelper, private val userName: String) {
    private val staticCategories = listOf("Financial", "School", "Health", "Shopping", "Games", "Travel", "Websites", "Social Media", "Misc")
    private var userCreatedCategories :MutableList<String> = ArrayList()
    private val mergedCategoriesList: MutableList<String> = ArrayList()
    private val tag: String = "Categories"

    @Composable
    /*
    fun DisplayCategories(){
        val context = LocalContext.current
    }*/

    fun getUserCategories(): List<String>{
        try {
            userCreatedCategories =
                databaseHelper.getAllCategories(databaseHelper.writeableDB, userName)
        } catch (ex: Exception){
            Log.e(tag, "Unable to access database to retrieve all categories.", ex)
        }

        mergedCategoriesList.addAll(staticCategories)
        mergedCategoriesList.addAll(userCreatedCategories)

        return mergedCategoriesList
    }
}