package com.rjulsaint.impasse
/*
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
   override fun onCreate(db : SQLiteDatabase?){
       val createPasswordTable = ("CREATE TABLE Password (id,webaddress,description,login,password,masterpassword)")
       db?.execSQL(createPasswordTable)
       val createMasterPasswordTable = ("CREATE TABLE MasterPassword (id,masterpassword)")
       db?.execSQL(createMasterPasswordTable)
   }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //"Not yet implemented"
    }
}*/