package com.comp3617.assignment2.DB

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.comp3617.assignment2.Model.Task

@Database (entities = [Task::class], version = 1, exportSchema = true)
abstract class TaskListDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    companion object {
        val databaseName = "taskdatabase"
        var taskListDatabase: TaskListDatabase? = null

        fun getInstance(context: Context): TaskListDatabase?{
            if (taskListDatabase == null){
                taskListDatabase = Room.databaseBuilder(context,
                    TaskListDatabase::class.java,
                    TaskListDatabase.databaseName)
                    .build()
            }
            return taskListDatabase
        }
    }
}