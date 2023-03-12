package com.borispuhacin.taskmaker.di

import android.app.Application
import androidx.room.Room
import com.borispuhacin.taskmaker.data.TaskDao
import com.borispuhacin.taskmaker.data.TaskDatabase
import com.borispuhacin.taskmaker.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesTaskDatabase(app: Application) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesTaskDao(database: TaskDatabase) = database.taskDao()

    @Provides
    fun providesRepository(taskDao: TaskDao) = TaskRepository(taskDao)
}