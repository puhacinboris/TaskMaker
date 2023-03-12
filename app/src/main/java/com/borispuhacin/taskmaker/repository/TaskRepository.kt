package com.borispuhacin.taskmaker.repository

import com.borispuhacin.taskmaker.data.Task
import com.borispuhacin.taskmaker.data.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks() : Flow<List<Task>> = taskDao.getAllTasks()

    fun getAllTasksOrderDesc() : Flow<List<Task>> = taskDao.getAllTasksOrderDesc()

    fun getTaskByPriorityAsc() : Flow<List<Task>> = taskDao.orderByPriorityAsc()

    fun getTaskByPriorityDesc() : Flow<List<Task>> = taskDao.orderByPriorityDesc()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}