package com.example.roomongit.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class Todo (@PrimaryKey(autoGenerate = true) val id:Int? = null, val task:String, val progress: String)

@Dao
interface TodoDao {

    @Insert
    fun add(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAll(): LiveData<List<Todo>>
}

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase(){
    abstract fun todoDao():TodoDao
}