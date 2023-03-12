package com.borispuhacin.taskmaker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borispuhacin.taskmaker.R
import com.borispuhacin.taskmaker.data.Task
import com.borispuhacin.taskmaker.databinding.ItemTaskBinding
import com.borispuhacin.taskmaker.ui.fragments.ListFragmentDirections

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddEditFragment(item)
            Navigation.findNavController(it).navigate(action)
        }
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.textViewTaskTitle.text = task.title

            if (task.body.isEmpty()) {
                binding.textViewTaskBody.visibility = View.GONE
            }

            binding.textViewTaskBody.text = task.body

            when (task.priority) {
                1 -> {
                    binding.viewPriority.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_low))
                }
                2 -> {
                    binding.viewPriority.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_medium))
                }
                3 -> {
                    binding.viewPriority.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_high))
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}