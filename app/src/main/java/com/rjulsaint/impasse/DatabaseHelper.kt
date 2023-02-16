package com.rjulsaint.impasse

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    private val tag : String = "DatabaseHelper"
    val writeableDB: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable =
            ("CREATE TABLE IF NOT EXISTS ImpasseUser (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT NOT NULL, masterPassword TEXT NOT NULL)")
        db?.execSQL(createUserTable)
        val createPasswordTable =
            ("CREATE TABLE IF NOT EXISTS ImpassePassword (id INTEGER PRIMARY KEY AUTOINCREMENT,webAddress TEXT NOT NULL,description TEXT NOT NULL,password TEXT NOT NULL,masterPassword TEXT NOT NULL REFERENCES ImpasseUser)")
        db?.execSQL(createPasswordTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ImpasseUser")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS ImpassePassword")
        onCreate(db)
    }

    fun addNewUser(db: SQLiteDatabase?, userName: String, masterPassword: String): Long? {
        return db?.insert(
            "ImpasseUser",
            null,
            contentValuesOf(Pair("masterPassword", masterPassword), Pair("userName", userName))
        )
    }

    fun addNewPassword(
        db: SQLiteDatabase?,
        webAddress: String,
        description: String,
        password: String,
        masterPassword: String
    ): Long? {
        return db?.insert(
            "ImpassePassword",
            null,
            contentValuesOf(
                Pair("webAddress", webAddress),
                Pair("description", description),
                Pair("password", password),
                Pair("masterPassword", masterPassword)
            )
        )
    }
/*
    private fun deleteAllPasswords(db: SQLiteDatabase) : Int{
        return db.delete("ImpassePassword",null, null)
    }
*/
    private fun deletePassword(db : SQLiteDatabase, webAddress: String, description: String): Int{
        return db.delete("ImpassePassword", "WebAddress = ? AND description = ?", arrayOf(webAddress, description))
    }

    fun getAllUserStoredPasswords(db: SQLiteDatabase, masterPassword: String) : MutableList<List<String>>{
        val result = db.query(
            "ImpassePassword",
            arrayOf("webAddress", "description", "password"),
            "masterPassword = ?",
            arrayOf(masterPassword),
            null,
            null,
            null,
            null
        )

        val recordsList : MutableList<List<String>> = mutableListOf()

        if(result.moveToFirst()){
            do{
                val record: MutableList<String> = mutableListOf(
                    result.getString(0),
                    result.getString(1),
                    result.getString(2)
                )
                recordsList.add(record)
            }while(result.moveToNext())
        }
        result.close()
        return recordsList
    }

    fun masterPasswordLogin(db: SQLiteDatabase, masterPassword: String, userName: String): Boolean {
        val result = db.query(
            "ImpasseUser",
            arrayOf("masterPassword"),
            "masterPassword = ? AND userName = ?",
            arrayOf(masterPassword, userName),
            null,
            null,
            null,
            null
        )

        result.moveToFirst()
        val valid = try {
            result.getString(0) == masterPassword
        }catch(ex: CursorIndexOutOfBoundsException){
            false
        }
        result.close()
        return valid
    }

    fun onDeletePress(builder : AlertDialog.Builder, /*tableClearDown : Boolean = false,*/setMessage : String, errorMessage : String, webAddress : String = "", description: String = "") : Int{
        builder.setMessage(setMessage)
        builder.setTitle("Alert!!")
        builder.setCancelable(false)
        var numOfRecordsDeleted = -1
        try {
            builder.setPositiveButton("Yes") {
                // When the user click yes button then app will close
                    _, _ ->
                 /*numOfRecordsDeleted = if(tableClearDown) {
                    this.deleteAllPasswords(writeableDB)
                } else {
                    this.deletePassword(writeableDB, webAddress, description)
                }*/
                numOfRecordsDeleted = this.deletePassword(writeableDB, webAddress, description)
            }
        } catch(ex : java.lang.Exception){
            Log.e(tag, errorMessage, ex)
        }

        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, _ -> dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
        return numOfRecordsDeleted
    }
}