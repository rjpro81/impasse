package com.rjulsaint.impasse

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    private val table: String = "Password"
    override fun onCreate(db: SQLiteDatabase?) {
        /*val createPasswordTable =
            ("CREATE TABLE Password (id INTEGER PRIMARY KEY AUTOINCREMENT,webaddress TEXT,description TEXT,login TEXT,password TEXT NOT NULL,masterpassword TEXT)")
        db?.execSQL(createPasswordTable)
        val createMasterPasswordTable =
            ("CREATE TABLE MasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterpassword TEXT REFERENCES Password)")*/
        val createMasterPasswordTable =
            ("CREATE TABLE IF NOT EXISTS MasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterpassword TEXT)")
        db?.execSQL(createMasterPasswordTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $table")
        onCreate(db)
    }

    /*fun addMasterPassword(db: SQLiteDatabase?, masterPassword : String){
        val createMasterPassword =
            ("INSERT INTO MasterPassword (masterpassword) values ($masterPassword)")
        db?.execSQL(createMasterPassword)
    }*/

    fun masterPasswordLogin(db: SQLiteDatabase?, masterPassword: String): Int? {
        val checkPassword =
            ("SELECT masterpassword from MasterPassword WHERE masterpassword = ?")
        val result = db?.rawQuery(checkPassword, arrayOf(masterPassword))
        result?.moveToFirst()
        //return result?.getString(1)
        result?.close()
        return result?.count
    }
}