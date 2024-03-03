package com.example.roomongit

import android.app.Application
import androidx.room.Room
import com.example.roomongit.dbnew.TodoDatabase

class MyApplication: Application() {
    lateinit var repo:TodoRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val dbnew = Room.databaseBuilder(this, TodoDatabase::class.java, "todo_database").build()
        repo = TodoRepository(dbnew)
    }

    companion object{
        private lateinit var instance:MyApplication
        fun getApp() = instance
    }
}