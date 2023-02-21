package com.rjulsaint.impasse

import androidx.compose.runtime.Composable

class Categories(databaseHelper: DatabaseHelper, userName: String) {
    private val staticCategories = listOf("Financial", "School", "Health", "Shopping", "Games", "Travel", "Websites", "Social Media", "Misc")
    private val userCreatedCategories = databaseHelper.getAllCategories(databaseHelper.writeableDB, userName)
    private val mergedCategoriesList: MutableList<String> = ArrayList()

    @Composable
    /*
    fun DisplayCategories(){
        val context = LocalContext.current
    }*/

    fun getUserCategories(): List<String>{
        mergedCategoriesList.addAll(staticCategories)
        mergedCategoriesList.addAll(userCreatedCategories)

        return mergedCategoriesList
    }
}