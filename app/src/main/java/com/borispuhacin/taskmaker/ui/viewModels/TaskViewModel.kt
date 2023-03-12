package com.borispuhacin.taskmaker.ui.viewModels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.borispuhacin.taskmaker.data.Task
import com.borispuhacin.taskmaker.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val taskEventChannel = Channel<TaskEvent>()

    val taskEvent = taskEventChannel.receiveAsFlow()

    val taskList = repository.getAllTasks().asLiveData()

    val taskListOrderDesc = repository.getAllTasksOrderDesc().asLiveData()

    val taskListByPriorityAsc = repository.getTaskByPriorityAsc().asLiveData()

    val taskListByPriorityDesc = repository.getTaskByPriorityDesc().asLiveData()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.insertTask(task)
            } catch (e: Exception) {
                Log.e("TASKS ERROR", "${e.message}")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
            } catch (e: Exception) {
                Log.e("TASKS ERROR", "${e.message}")
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch{
            try {
                repository.deleteAllTasks()
            } catch (e: Exception) {
                Log.e("TASKS ERROR", "${e.message}")
            }
        }
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        taskEventChannel.send(TaskEvent.ShowUndoDeletedTaskMessage(task))
    }

    fun onUndoSwipeDelete(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    sealed class TaskEvent {
        data class ShowUndoDeletedTaskMessage(val task: Task) : TaskEvent()
    }
}