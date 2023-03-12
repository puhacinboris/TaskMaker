package com.borispuhacin.taskmaker.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val title: String,
    val body: String,
    val priority: Int
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
