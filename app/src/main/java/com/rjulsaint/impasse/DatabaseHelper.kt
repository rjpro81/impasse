package com.rjulsaint.impasse

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable =
            ("CREATE TABLE IF NOT EXISTS ImpasseUser (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT NOT NULL, masterPassword TEXT NOT NULL)")
        db?.execSQL(createUserTable)
        val createPasswordTable =
            ("CREATE TABLE IF NOT EXISTS ImpassePassword (id INTEGER PRIMARY KEY AUTOINCREMENT,webAddress TEXT,description TEXT,login TEXT,password TEXT NOT NULL,masterPassword TEXT NOT NULL REFERENCES ImpasseUser)")
        db?.execSQL(createPasswordTable)

        db?.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ImpasseUser")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS ImpassePassword")
        onCreate(db)

        db?.close()
    }

    fun addNewUser(db: SQLiteDatabase?, userName: String,masterPassword: String) : Long? {
        val result = db?.insert("ImpasseUser",null, contentValuesOf(Pair("masterPassword",masterPassword), Pair("userName", userName)))
        db?.close()
        return result
    }
/*
    fun addNewPassword(db: SQLiteDatabase?, webAddress: String, description: String, password: String, masterPassword: String) : Long? {
        val result = db?.insert("ImpassePassword",null, contentValuesOf(Pair("webAddress",webAddress), Pair("description", description), Pair("password", password), Pair("masterPassword", masterPassword)))
        db?.close()
        return result
    }
*/
    fun deleteAll(db: SQLiteDatabase?){
        db?.execSQL("DROP TABLE IF EXISTS ImpasseUser")
        db?.execSQL("DROP TABLE IF EXISTS ImpassePassword")

        db?.close()
    }

    fun getAllUserStoredPasswords(db: SQLiteDatabase?, masterPassword: String) : MutableList<List<String>>{
        val result = db?.query(
            true,
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

        while(result != null) {
            val record: MutableList<String> = mutableListOf(
                result.getString(0),
                result.getString(1),
                result.getString(2)
            )
            recordsList.add(record)
            result.moveToNext()
        }

        result?.close()
        return recordsList

    }

    fun masterPasswordLogin(db: SQLiteDatabase?, masterPassword: String, userName: String): Boolean {
        val result = db?.query(
            "ImpasseUser",
            arrayOf("masterPassword"),
            "masterPassword = ? AND userName = ?",
            arrayOf(masterPassword, userName),
            null,
            null,
            null,
            null
        )

        result?.moveToFirst()
        val valid = try {
            result?.getString(0) == masterPassword
        }catch(ex: CursorIndexOutOfBoundsException){
            false
        }

        result?.close()
        return valid
    }
}