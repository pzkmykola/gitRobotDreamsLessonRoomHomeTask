package com.example.roomongit.dbnew

import androidx.room.RoomDatabase


data class TodoFB (
    var id: String = "",     // This is the id of the document in the database
    var title: String = "",  // This is the title of the task
    var note: String = "",   // Low, Medium, High
    var date: String = ""    // 2021-01-01
)


interface TodoDao {

    fun add(todo: TodoFB)

    fun remove(todo: TodoFB)
}

abstract class TodoFirebase : RoomDatabase(){
    abstract fun todoDao():TodoDao
}
