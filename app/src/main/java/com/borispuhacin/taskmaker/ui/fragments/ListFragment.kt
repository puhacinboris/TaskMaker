package com.borispuhacin.taskmaker.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.borispuhacin.taskmaker.R
import com.borispuhacin.taskmaker.data.Task
import com.borispuhacin.taskmaker.databinding.FragmentListBinding
import com.borispuhacin.taskmaker.ui.adapters.TaskListAdapter
import com.borispuhacin.taskmaker.ui.viewModels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), MenuProvider {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by activityViewModels()
    private lateinit var listAdapter: TaskListAdapter
    private var isTaskTrayEmpty: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)

        listAdapter = TaskListAdapter()

        binding.apply {
            recycleViewTasks.apply {
                adapter = listAdapter
                setHasFixedSize(true)
            }

            ItemTouchHelper( object : ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = listAdapter.currentList[viewHolder.adapterPosition]
                    taskViewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recycleViewTasks)

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addEditFragment)
            }
        }

        taskViewModel.taskListOrderDesc.observe(viewLifecycleOwner) {
            emptyTray(it)
            listAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            taskViewModel.taskEvent.collect { event ->
                when(event) {
                    is TaskViewModel.TaskEvent.ShowUndoDeletedTaskMessage ->
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                taskViewModel.onUndoSwipeDelete(event.task)
                            }
                            .show()
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.action_sort_older_first -> {
                taskViewModel.taskList.observe(viewLifecycleOwner) {
                    listAdapter.submitList(it)
                }
                return true
            }
            R.id.action_sort_newer_first -> {
                taskViewModel.taskListOrderDesc.observe(viewLifecycleOwner) {
                    listAdapter.submitList(it)
                }
                return true
            }
            R.id.action_sort_from_high_to_low -> {
                taskViewModel.taskListByPriorityDesc.observe(viewLifecycleOwner) {
                    listAdapter.submitList(it)
                }
                return true
            }
            R.id.action_sort_from_low_to_high -> {
                taskViewModel.taskListByPriorityAsc.observe(viewLifecycleOwner) {
                    listAdapter.submitList(it)
                }
                return true
            }
            R.id.action_delete_all -> {
                deleteAlertDialog()
                return true
            }
        }
        return false
    }

    private fun emptyTray(list: List<Task>) {
        if (list.isEmpty()) {
            binding.textViewEmptyTray.visibility = View.VISIBLE
        } else {
            binding.textViewEmptyTray.visibility = View.GONE
        }

    }

    private fun deleteAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Delete all tasks")
            setMessage("Do you want to delete all tasks?")
            setPositiveButton("Yes") {_,_ -> taskViewModel.deleteAllTasks()}
            setNegativeButton("No") {_,_ ->}
            create()
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}