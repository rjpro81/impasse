package com.rjulsaint.impasse

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable =
            ("CREATE TABLE IF NOT EXISTS ImpasseUser (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT NOT NULL, masterPassword TEXT NOT NULL REFERENCES MasterPassword)")
        db?.execSQL(createUserTable)
        val createPasswordTable =
            ("CREATE TABLE IF NOT EXISTS ImpassePassword (id INTEGER PRIMARY KEY AUTOINCREMENT,webAddress TEXT,description TEXT,login TEXT,password TEXT NOT NULL,masterPassword TEXT)")
        db?.execSQL(createPasswordTable)

        val createMasterPasswordTable =
            ("CREATE TABLE IF NOT EXISTS ImpasseMasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterPassword TEXT NOT NULL)")
        db?.execSQL(createMasterPasswordTable)

        db?.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ImpasseUser")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS ImpassePassword")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS ImpasseMasterPassword")
        onCreate(db)

        db?.close()
    }
/*
    fun addMasterPassword(db: SQLiteDatabase?, masterPassword : String) : Long? {
        val result = db?.insert("ImpasseMasterPassword",null, contentValuesOf(Pair("masterPassword",masterPassword)))
        db?.close()
        return result
    }
*/
    fun masterPasswordLogin(db: SQLiteDatabase?, masterPassword: String): Boolean {
        val result = db?.query(
            true,
            "ImpasseMasterPassword",
            arrayOf("masterPassword"),
            "masterPassword = ?",
            arrayOf(masterPassword),
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