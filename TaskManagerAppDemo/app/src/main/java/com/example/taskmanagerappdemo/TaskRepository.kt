package com.example.taskmanagerappdemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class TaskRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context) // Initialize DatabaseHelper for database operations.

    // Method to insert a new task into the database.
    fun insertTask(task: Task) {
        val db = dbHelper.writableDatabase // Get writable database instance.
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, task.title) // Add task title to ContentValues.
            put(DatabaseHelper.COLUMN_DESCRIPTION, task.description) // Add task description to ContentValues.
            put(DatabaseHelper.COLUMN_COMPLETED, if (task.completed) 1 else 0) // Add task completed status.
            put(DatabaseHelper.COLUMN_HOUR, task.hour) // Add task hour to ContentValues.
            put(DatabaseHelper.COLUMN_MINUTE, task.minute) // Add task minute to ContentValues.
        }
        db.insert(DatabaseHelper.TABLE_TASKS, null, values) // Insert values into the tasks table.
        db.close() // Close the database connection.
    }

    // Method to retrieve all tasks from the database.
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>() // Create a mutable list to store tasks.
        val db = dbHelper.readableDatabase // Get readable database instance.

        // Query to select all tasks from the tasks table.
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_TASKS, null, null, null, null, null, null)

        // Log the column names in the tasks table.
        val columnNames = cursor.columnNames.joinToString(", ")
        Log.d("TaskRepository", "Columns: $columnNames")

        // Get column indices for easier access.
        val titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)
        val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
        val completedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLETED)
        val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val hourIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HOUR) // Get task hour index.
        val minuteIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MINUTE) // Get task minute index.

        // Loop through the cursor to extract task data.
        while (cursor.moveToNext()) {
            // Check that indices are valid before accessing them.
            if (titleIndex != -1 && descriptionIndex != -1 && completedIndex != -1 && idIndex != -1 && hourIndex != -1 && minuteIndex != -1) {
                val id = cursor.getLong(idIndex) // Get the task ID.
                val title = cursor.getString(titleIndex) // Get the task title.
                val description = cursor.getString(descriptionIndex) // Get the task description.
                val completed = cursor.getInt(completedIndex) == 1 // Check if the task is completed.
                val hour = cursor.getInt(hourIndex) // Get the task hour.
                val minute = cursor.getInt(minuteIndex) // Get the task minute.

                // Create a Task object and add it to the task list.
                taskList.add(Task(id, title, description, completed, hour, minute))
            } else {
                Log.e("TaskRepository", "Invalid column indices") // Log error if indices are invalid.
            }
        }
        cursor.close() // Close the cursor to release resources.
        db.close() // Close the database connection.
        return taskList // Return the list of tasks.
    }

    // Method to delete a task by its ID.
    fun deleteTask(taskId: Long) {
        val db = dbHelper.writableDatabase // Get writable database instance.
        db.delete(DatabaseHelper.TABLE_TASKS, "${DatabaseHelper.COLUMN_ID}=?", arrayOf(taskId.toString())) // Delete the task from the database.
        db.close() // Close the database connection.
    }

// Method to update a task

    fun updateTask(task:Task)
    {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE,task.title)
            put(DatabaseHelper.COLUMN_DESCRIPTION,task.description)
            put(DatabaseHelper.COLUMN_COMPLETED, if (task.completed) 1 else 0)
            put(DatabaseHelper.COLUMN_HOUR,task.hour)
            put(DatabaseHelper.COLUMN_MINUTE,task.minute)

        }

        val rowsAffected=db.update(DatabaseHelper.TABLE_TASKS,values ,"${DatabaseHelper.COLUMN_ID}=?", arrayOf(task.id.toString()))

        if(rowsAffected >0)
        {
            Log.d("TaskRepository", "Task Updated")
        }

        else
        {
            Log.d("TaskRepository", "Task not Updated")
        }

        db.close()
    }
    fun checkDatabaseStructure()
    {
        val db= dbHelper.readableDatabase
        val cursor=db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_TASKS} LIMIT 1",null)

        val columnNames = cursor.columnNames.joinToString(", ")
        Log.d("TaskRepository","Columns in table: $columnNames")
        cursor.close()
        db.close()
    }
}
