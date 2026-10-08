package com.example.exp9.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String
)

/**
 * SQLite helper – creates exp9.db with a single table `notes` and exposes CRUD operations.
 * File on device: /data/data/com.example.exp9/databases/exp9.db
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_CONTENT TEXT,
                $COL_CREATED TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    // ---------- CREATE ----------
    fun insertNote(title: String, content: String): Long {
        val stamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_CREATED, stamp)
        }
        return writableDatabase.insert(TABLE, null, values)
    }

    // ---------- READ ----------
    fun getAllNotes(): List<Note> {
        val list = mutableListOf<Note>()
        readableDatabase.rawQuery(
            "SELECT $COL_ID, $COL_TITLE, $COL_CONTENT, $COL_CREATED FROM $TABLE ORDER BY $COL_ID DESC",
            null
        ).use { c ->
            while (c.moveToNext()) {
                list.add(
                    Note(
                        id = c.getLong(0),
                        title = c.getString(1),
                        content = c.getString(2) ?: "",
                        createdAt = c.getString(3)
                    )
                )
            }
        }
        return list
    }

    // ---------- UPDATE ----------
    fun updateNote(id: Long, title: String, content: String): Int {
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
        }
        return writableDatabase.update(TABLE, values, "$COL_ID = ?", arrayOf(id.toString()))
    }

    // ---------- DELETE ----------
    fun deleteNote(id: Long): Int =
        writableDatabase.delete(TABLE, "$COL_ID = ?", arrayOf(id.toString()))

    fun deleteAll(): Int = writableDatabase.delete(TABLE, null, null)

    companion object {
        const val DB_NAME = "exp9.db"
        private const val DB_VERSION = 1
        const val TABLE = "notes"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_CONTENT = "content"
        private const val COL_CREATED = "created_at"
    }
}
