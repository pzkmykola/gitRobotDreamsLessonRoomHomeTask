package com.example.roomongit.dbnew

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity
data class TodoFB (@PrimaryKey(autoGenerate = true) val id:Int? = null, val title:String, val note: String, val date: String )

@Dao
interface TodoDao {

    @Insert
    fun add(todo: TodoFB)

    @Delete
    fun delete(todo: TodoFB)

    @Query("SELECT * FROM todoFB")
    fun getAll(): LiveData<List<TodoFB>>
}

@Database(entities = [TodoFB::class], version = 2)
abstract class TodoDatabase: RoomDatabase(){
    abstract fun todoDao():TodoDao
}