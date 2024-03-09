package com.example.roomongit.dbnew

import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.RoomDatabase

@Entity
data class TodoFB (
    var id: String = "",     // This is the id of the document in the database
    var title: String = "",  // This is the title of the task
    var note: String = "",   // Low, Medium, High
    var date: String = ""    // 2021-01-01
)

//@Dao
interface TodoDao {
    //@Insert
    fun add(title: String, note: String, date:String):Boolean
    //@Delete
    fun remove(todo: TodoFB)

    fun getAll(): LiveData<List<TodoFB>>
}

abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDao():TodoDao
}
