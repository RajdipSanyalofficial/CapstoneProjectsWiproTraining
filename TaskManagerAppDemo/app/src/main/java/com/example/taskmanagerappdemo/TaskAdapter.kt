package com.example.taskmanagerappdemo



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onDeleteClick: (Task) -> Unit,
    private val onUpdateClick: (Task) -> Unit // Add onUpdateClick to the constructor
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    // Method to create a new ViewHolder for the RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the item layout for each task.
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView) // Return a new TaskViewHolder instance.
    }

    // Method to bind data to the ViewHolder.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Get the task at the current position.
        val task = getItem(position)
        // Bind the task data to the ViewHolder.
        holder.bind(task, onDeleteClick, onUpdateClick) // Pass onDeleteClick and onUpdateClick
    }

    // ViewHolder class for displaying a single task item.
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // References to the TextViews and Buttons in the item layout.
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        private val textViewDescription: TextView =
            itemView.findViewById(R.id.textViewTaskDescription)
        private val textViewTaskTime: TextView =
            itemView.findViewById(R.id.textViewTaskTime) // Add reference for the time TextView
        private val buttonDeleteTask: Button = itemView.findViewById(R.id.buttonDeleteTask)
        private val buttonUpdateTask: Button = itemView.findViewById(R.id.buttonUpdateTask)

        // Method to bind the task data to the views.
        fun bind(task: Task, onDeleteClick: (Task) -> Unit, onUpdateClick: (Task) -> Unit) {
            // Set the task title and description in the corresponding TextViews.
            textViewTitle.text = task.title
            textViewDescription.text = task.description

            // Create a Calendar instance to store the task time.
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, task.hour)
                set(Calendar.MINUTE, task.minute)
            }

            // Format the time using SimpleDateFormat for 12-hour time with AM/PM.
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val formattedTime = timeFormat.format(calendar.time)

            textViewTaskTime.text = "Time: $formattedTime" // Display formatted time with AM/PM.

            // Handle the delete button click by invoking the provided lambda function.
            buttonDeleteTask.setOnClickListener {
                onDeleteClick(task) // Call the onDeleteClick function with the current task.
            }

            // Handle the update button click by invoking the provided lambda function.
            buttonUpdateTask.setOnClickListener {
                onUpdateClick(task) // Call the onUpdateClick function with the current task.
            }
        }
    }

    // DiffCallback class for efficiently updating the RecyclerView's data.
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        // Check if two tasks are the same based on their IDs.
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if the contents of two tasks are the same.
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description &&
                    oldItem.completed == newItem.completed &&
                    oldItem.hour == newItem.hour &&
                    oldItem.minute == newItem.minute
        }
    }
}