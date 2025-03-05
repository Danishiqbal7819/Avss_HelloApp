package com.example.myapp.database

import SqlDbWIthMvvm.Model.Users
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRST_NAME = "firstName"
        private const val COLUMN_MIDDLE_NAME = "middleName"
        private const val COLUMN_LAST_NAME = "lastName"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME ( $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                  $COLUMN_FIRST_NAME TEXT, 
                    $COLUMN_MIDDLE_NAME TEXT, 
                $COLUMN_LAST_NAME TEXT, 
                $COLUMN_PHONE TEXT, 
                $COLUMN_EMAIL TEXT,
                $COLUMN_IMAGE BLOB
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(
        firstName: String,
        middleName: String,
        lastName: String,
        phone: String,
        email: String,
        image: ByteArray
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_MIDDLE_NAME, middleName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
            put(COLUMN_IMAGE, image)
        }

        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }

    fun getAllUsers(): List<Users> {
        val userList = mutableListOf<Users>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, firstName, middleName, lastName, phone, email,image FROM users",
            null
        )

        while (cursor.moveToNext()) {
            val user = Users(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getBlob(6)
            )
            userList.add(user)
        }
        cursor.close()
        return userList
    }

    fun userbysearch(name: String): List<Users> {
        val db = readableDatabase
        val userList1 = mutableListOf<Users>()
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_FIRST_NAME LIKE ?",
            arrayOf("$name%")
        )
        while (cursor.moveToNext()) {
            val user = Users(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getBlob(6),
            )
            userList1.add(user)
        }
        return userList1
    }

    fun getUserById(userId: Int): Users? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, firstName, middleName, lastName, phone, email, image FROM users WHERE id = ?",
            arrayOf(userId.toString())
        )

        var user: Users? = null
        if (cursor.moveToFirst()) {
            user = Users(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getBlob(6)
            )
        }
        cursor.close()
        return user
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun deleteById(userId: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(userId.toString()))
        db.close()
        return success > 0
    }

    fun updateUser(
        userId: Int,
        firstName: String,
        middleName: String,
        lastName: String,
        phone: String,
        email: String,
        image: ByteArray
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_MIDDLE_NAME, middleName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
            put(COLUMN_IMAGE, image)
        }
        val success =
            db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(userId.toString()))
        db.close()
        return success > 0
    }
}
