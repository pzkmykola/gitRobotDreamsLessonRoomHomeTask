package com.example.roomongit

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.roomongit.db.TodoDatabase

class MyApplication: Application() {
    lateinit var repo:TodoRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val db = Room.databaseBuilder(this, TodoDatabase::class.java, "todo_database").build()
        repo = TodoRepository(db)
    }

    companion object{
        private lateinit var instance:MyApplication
        fun getApp() = instance
    }
}