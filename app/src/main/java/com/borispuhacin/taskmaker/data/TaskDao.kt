package com.borispuhacin.taskmaker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table")
    fun getAllTasks() : Flow<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasksOrderDesc() : Flow<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY priority ASC")
    fun orderByPriorityAsc() : Flow<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY priority DESC")
    fun orderByPriorityDesc() : Flow<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()
}