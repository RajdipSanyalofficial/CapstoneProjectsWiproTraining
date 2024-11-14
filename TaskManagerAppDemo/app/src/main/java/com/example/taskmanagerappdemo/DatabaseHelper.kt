package com.example.taskmanagerappdemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task_manager.db" // Name of the database.
        private const val DATABASE_VERSION = 1 // Current version of the database.
        const val TABLE_TASKS = "tasks" // Name of the tasks table.
        const val COLUMN_ID = "_id" // Column name for the ID.
        const val COLUMN_TITLE = "title" // Column name for the task title.
        const val COLUMN_DESCRIPTION = "description" // Column name for the task description.
        const val COLUMN_COMPLETED = "completed" // Column name for the completion status.
        const val COLUMN_HOUR = "hour" // New column for task hour
        const val COLUMN_MINUTE = "minute" // New column for task minut
    }


    // SQL command to create the tasks table

    private val CREATE_TABLE_TASKS = """
        CREATE TABLE $TABLE_TASKS (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_TITLE TEXT,
        $COLUMN_DESCRIPTION TEXT,
        $COLUMN_COMPLETED INTEGER,
        $COLUMN_HOUR INTEGER,
        $COLUMN_MINUTE INTEGER
        )
    
    """.trimIndent() // Trim any extra whitespace for cleaner SQL.

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_TASKS) // Create the tasks table
    }

    // Method called when the database needs to be upgraded.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing tasks table if it exists to reset the IDs.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        // Recreate the tasks table with IDs starting from 1.
        onCreate(db)
    }
}
