package com.comp3617.assignment2.DB

import android.arch.persistence.room.*
import com.comp3617.assignment2.Model.Task


@Dao
interface TaskDao{

    @get:Query("SELECT * FROM task")
    val all: List<Task>

    @Query("SELECT * FROM task WHERE uid = :uid")
    fun findById(uid: Long): Task?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task?)

    @Update
    fun update(task: Task?)

    @Delete
    fun delete(task: Task?)
}
