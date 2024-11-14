package com.example.taskmanagerappdemo



import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagerappdemo.databinding.ActivityMainBinding

// MainActivity class for managing the user interface and interactions in the Task Manager app.
class MainActivity : AppCompatActivity() {
    // Late init properties for view binding and the task adapter.
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    // Lazy initialization of the TaskRepository for database operations.
    private val taskRepository: TaskRepository by lazy { TaskRepository(this) }

    // onCreate method is called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding and set it as the content view.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView and its adapter.
        setupRecyclerView()
        // Set up button listeners for user interactions.
        setupButtons()
        // Load existing tasks from the database.
        loadTasks()
    }

    // Method to set up the RecyclerView for displaying tasks.
    private fun setupRecyclerView() {
        // Initialize the TaskAdapter with lambda functions to handle task deletion and updating.
        taskAdapter = TaskAdapter(
            onDeleteClick = { task -> deleteTask(task) },
            onUpdateClick = { task -> updateTask(task) } // Pass the updateTask function here
        )

        // Set the layout manager for the RecyclerView.
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        // Set the adapter for the RecyclerView.
        binding.recyclerViewTasks.adapter = taskAdapter
    }


    // Method to set up button listeners.
    private fun setupButtons() {
        // Set a click listener on the add task button to call addTask() when clicked.
        binding.buttonAddTask.setOnClickListener { addTask() }
    }

    // Method to add a new task to the database.
    private fun addTask() {
        // Retrieve title and description from the EditText fields.
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()

        // Get the hour and minute from the TimePicker
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute

        // Log the selected time for debugging (optional)
        Log.d("MainActivity", "Selected time: $hour:$minute")

        // Check if both title and description are not empty.
        if (title.isNotEmpty() && description.isNotEmpty()) {
            // Create a new Task object, include hour and minute.
            val task = Task(0, title, description, false, hour, minute)
            // Insert the new task into the database.
            taskRepository.insertTask(task)
            // Reload the list of tasks after insertion.
            loadTasks()
        }
    }




    // Method to delete a specific task passed as a parameter.
    private fun deleteTask(task: Task) {
        // Delete the task using its ID.
        taskRepository.deleteTask(task.id)
        // Reload the list of tasks after deletion.
        loadTasks()
    }


    private fun updateTask(task: Task) {
        // Create an AlertDialog to update the task
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_task, null)

        // Get references to the EditText fields in the dialog layout
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextUpdateTitle)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextUpdateDescription)


        // Set the current task details in the EditTexts
        editTextTitle.setText(task.title)
        editTextDescription.setText(task.description)

        // Build the dialog
        builder.setView(dialogView)
            .setTitle("Update Task")
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = editTextTitle.text.toString()
                val updatedDescription = editTextDescription.text.toString()

                // Check if the updated title and description are not empty
                if (updatedTitle.isNotEmpty() && updatedDescription.isNotEmpty()) {
                    // Update the task object with the new details
                    task.title = updatedTitle
                    task.description = updatedDescription

                    //task.completed=task.completed

                    //Log.d("TaskRepository","Updating task: $task")

                    // Update the task in the database using the repository
                    taskRepository.updateTask(task)

                    // Reload the tasks from the database to reflect changes in RecyclerView
                    loadTasks()
                }
            }
            .setNegativeButton("Cancel", null) // Dismiss the dialog on Cancel
            .show() // Display the dialog
    }

    // Method to load tasks from the database and update the RecyclerView.
    private fun loadTasks() {
        // Retrieve all tasks from the repository.
        val tasks = taskRepository.getAllTasks()
        // Log the number of tasks for debugging purposes.
        Log.d("MainActivity", "Number of tasks: ${tasks.size}")
        // Submit the list of tasks to the adapter for display in the RecyclerView.
        taskAdapter.submitList(tasks)

        // Force the adapter to refresh the data in the RecyclerView
        taskAdapter.notifyDataSetChanged()
    }
}
