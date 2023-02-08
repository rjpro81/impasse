package com.rjulsaint.impasse

//import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//import androidx.core.content.contentValuesOf

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    private val table: String = "Password"
    val readableDB: SQLiteDatabase = this.readableDatabase
    val writableDB: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        /*val createPasswordTable =
            ("CREATE TABLE Password (id INTEGER PRIMARY KEY AUTOINCREMENT,webAddress TEXT,description TEXT,login TEXT,password TEXT NOT NULL,masterPassword TEXT)")
        db?.execSQL(createPasswordTable)
        val createMasterPasswordTable =
            ("CREATE TABLE MasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterPassword TEXT REFERENCES Password)")*/
        val createMasterPasswordTable =
            ("CREATE TABLE IF NOT EXISTS MasterPassword (id INTEGER PRIMARY KEY AUTOINCREMENT,masterPassword TEXT)")
        db?.execSQL(createMasterPasswordTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $table")
        onCreate(db)
    }
/*
    fun addMasterPassword(db: SQLiteDatabase?, masterPassword : String) : Long? {
        val contentValues = ContentValues()
        return db?.insert("MasterPassword",null, contentValuesOf(Pair("masterPassword",masterPassword)))
    }
*/
    fun masterPasswordLogin(db: SQLiteDatabase?, masterPassword: String): Boolean {
        val result = db?.query(
            true,
            "MasterPassword",
            arrayOf("masterPassword"),
            "masterPassword = ?",
            arrayOf(masterPassword),
            null,
            null,
            null,
            null
        )
        var index = -1
        result?.moveToFirst()
        if (result?.getColumnIndex("masterpassword")!! >= 0) {
            index = result.getColumnIndex("masterpassword")
        }
        val valid = result.getString(index) != null
        result.close()
        return valid
    }
}