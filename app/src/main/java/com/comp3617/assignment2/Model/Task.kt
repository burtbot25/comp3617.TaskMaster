package com.comp3617.assignment2.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task")
data class Task(@PrimaryKey(autoGenerate = true) var uid: Long? = null,
                @ColumnInfo(name = "title") var title: String?,
                @ColumnInfo(name = "desc") var desc: String?,
                @ColumnInfo(name = "priority") var priority: String?,
                @ColumnInfo(name = "date") var date: String?,
                @ColumnInfo(name = "reminderOn") var reminderOn : Boolean?
)