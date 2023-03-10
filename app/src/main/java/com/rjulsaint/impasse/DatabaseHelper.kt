package com.rjulsaint.impasse

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "ImpasseDatabase", null, 1) {
    //val writeableDB: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable =
            ("CREATE TABLE IF NOT EXISTS ImpasseUser (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT NOT NULL, masterPassword TEXT NOT NULL)")
        db.execSQL(createUserTable)
        val createPasswordTable =
            ("CREATE TABLE IF NOT EXISTS ImpassePassword (id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT NOT NULL REFERENCES Categories, passUserName TEXT, password TEXT NOT NULL,masterPassword TEXT NOT NULL REFERENCES ImpasseUser, userName TEXT NOT NULL REFERENCES ImpasseUser)")
        db.execSQL(createPasswordTable)
        val createCategoryTable =
            ("CREATE TABLE IF NOT EXISTS Categories (id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT NOT NULL, userName TEXT REFERENCES ImpasseUser)")
        db.execSQL(createCategoryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ImpasseUser")
        onCreate(db)
        db.execSQL("DROP TABLE IF EXISTS ImpassePassword")
        onCreate(db)
        db.execSQL("DROP TABLE IF EXISTS Categories")
        onCreate(db)
    }

    fun addNewUser(db: SQLiteDatabase?, userName: String, masterPassword: String): Long? {
        return db?.insert(
            "ImpasseUser",
            null,
            contentValuesOf(Pair("masterPassword", masterPassword), Pair("userName", userName))
        )
    }

    fun updatePassword(db: SQLiteDatabase, oldCategory: String, oldPassUserName: String, newCategory: String,newUserName: String,newPassword: String, sessionUserName: String, sessionPassword: String): Int {
        return db.update(
            "ImpassePassword",
            contentValuesOf(Pair("category", newCategory), Pair("passUserName", newUserName), Pair("password", newPassword)),
            "userName = ? AND masterPassword = ? AND passUserName = ? AND category = ?",
            arrayOf(sessionUserName, sessionPassword, oldPassUserName, oldCategory)
        )
    }

    fun getAllCategories(db: SQLiteDatabase, userName: String? = null): MutableList<String>{
        val result = db.query(
            "Categories",
            arrayOf("category"),
            "userName = ?",
            arrayOf(userName),
            null,
            null,
            null,
            null
        )

        val recordsList: MutableList<String> = ArrayList()

        if (result.moveToFirst()) {
            do {
                val category: String = result.getString(0)
                recordsList.add(category)
            } while (result.moveToNext())
        }
        result.close()
        return recordsList
    }

    fun addNewPassword(
        db: SQLiteDatabase?,
        category: String,
        passUserName: String,
        password: String,
        masterPassword: String,
        userName: String
    ): Long? {
        return db?.insert(
            "ImpassePassword",
            null,
            contentValuesOf(
                Pair("category", category),
                Pair("passUserName", passUserName),
                Pair("password", password),
                Pair("masterPassword", masterPassword),
                Pair("userName", userName)
            )
        )
    }


    fun deleteAllPasswords(db: SQLiteDatabase) : Int{
        return db.delete("ImpassePassword",null, null)
    }

    fun deletePassword(db: SQLiteDatabase, category: String, passUserName: String, sessionUserName: String, sessionMasterPassword: String): Int {
        return db.delete(
            "ImpassePassword",
            "category = ? AND passUserName = ? AND userName = ? AND masterPassword = ?",
            arrayOf(category, passUserName, sessionUserName, sessionMasterPassword)
        )
    }

    fun getAllUserStoredPasswords(
        db: SQLiteDatabase,
        userName: String,
        masterPassword: String
    ): MutableList<List<String>> {
        val result = db.query(
            "ImpassePassword",
            arrayOf("category", "passUserName", "password"),
            "masterPassword = ? AND userName = ?",
            arrayOf(masterPassword, userName),
            null,
            null,
            "passUserName ASC",
            null
        )

        val recordsList: MutableList<List<String>> = mutableListOf()

        if (result.moveToFirst()) {
            do {
                val record: MutableList<String> = mutableListOf(
                    result.getString(0),
                    result.getString(1),
                    result.getString(2),
                )
                recordsList.add(record)
            } while (result.moveToNext())
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
        } catch (ex: CursorIndexOutOfBoundsException) {
            false
        }
        result.close()
        return valid
    }

    fun getImpasseUser(db: SQLiteDatabase): MutableList<List<String>>{
        val result = db.query(
            "ImpasseUser",
            arrayOf("userName", "masterPassword"),
            null,
            null,
            null,
            null,
            null,
            null
        )

        val recordsList: MutableList<List<String>> = mutableListOf()

        if (result.moveToFirst()) {
            do {
                val record: MutableList<String> = mutableListOf(
                    result.getString(0),
                    result.getString(1),
                )
                recordsList.add(record)
            } while (result.moveToNext())
        }
        result.close()
        return recordsList
    }

    fun updateUserProfile(db: SQLiteDatabase, oldUserName: String, oldMasterPassword: String, newUserName: String, newMasterPassword: String):Int{
        return db.update(
            "ImpasseUser",
            contentValuesOf(Pair("userName", newUserName), Pair("masterPassword", newMasterPassword)),
            "userName = ? AND masterPassword = ?",
            arrayOf(oldUserName, oldMasterPassword)
        )
    }

}