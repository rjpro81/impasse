package com.rjulsaint.impasse
/*
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    val table : String = "Password"
   override fun onCreate(db : SQLiteDatabase?){
       val createPasswordTable = ("CREATE TABLE Password (id INTEGER PRIMARY KEY AUTOINCREMENT,webaddress TEXT,description TEXT,login TEXT,password TEXT NOT NULL,masterpassword TEXT)")
       db?.execSQL(createPasswordTable)
       val createMasterPasswordTable = ("CREATE TABLE MasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterpassword TEXT REFERENCES Password)")
       db?.execSQL(createMasterPasswordTable)
   }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $table")
        onCreate(db)
    }
}*/