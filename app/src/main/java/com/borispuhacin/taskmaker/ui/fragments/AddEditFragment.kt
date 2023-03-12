package com.borispuhacin.taskmaker.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.borispuhacin.taskmaker.R
import com.borispuhacin.taskmaker.data.Task
import com.borispuhacin.taskmaker.databinding.FragmentAddEditBinding
import com.borispuhacin.taskmaker.ui.MainActivity
import com.borispuhacin.taskmaker.ui.viewModels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TaskViewModel by activityViewModels()
    private val args: AddEditFragmentArgs by navArgs()
    private var isInEditMode: Boolean = false
    private val mainActivity: MainActivity
        get() = activity as MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)

        if (args.task != null) {
            isInEditMode = true
            mainActivity.supportActionBar?.title = "Edit task"
            val sentTask = args.task!!
            setupEditTask(sentTask)
        } else {
            mainActivity.supportActionBar?.title = "Add task"
        }
    }

    private fun setupEditTask(task: Task) {
        binding.apply {
            editTextAddTitle.setText(task.title)
            editTextAddBody.setText(task.body)

            when (task.priority) {
                1 -> radioGroupPriorities.check(R.id.radioButton_priority_low)
                2 -> radioGroupPriorities.check(R.id.radioButton_priority_medium)
                3 -> radioGroupPriorities.check(R.id.radioButton_priority_high)
            }
        }
    }

    private fun getTaskInfo(): Task {
        binding.editTextAddTitle.requestFocus()
        val title = binding.editTextAddTitle.text.toString()

        if (title.isEmpty()) {
            binding.editTextAddTitle.error = "* Title is required"
        } else {
            binding.editTextAddTitle.error = null
        }

        val body = binding.editTextAddBody.text.toString().trim()

        val priority = when (binding.radioGroupPriorities.checkedRadioButtonId) {
            R.id.radioButton_priority_low -> 1
            R.id.radioButton_priority_medium -> 2
            else -> 3
        }

        return Task(title, body, priority)
    }

    private fun updateTask(updatedTask: Task) {
        updatedTask.id = args.task?.id!!
        sharedViewModel.updateTask(updatedTask)
        findNavController().navigateUp()
    }

    private fun insertTask(task: Task) {
        sharedViewModel.insertTask(task)
        findNavController().navigateUp()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.action_save_task) {
            val task = getTaskInfo()
            return if (isInEditMode) {
                updateTask(task)
                true
            } else {
                insertTask(task)
                true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}