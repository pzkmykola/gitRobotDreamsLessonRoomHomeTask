package com.example.roomongit

import com.example.roomongit.dbnew.TodoDatabase
import com.example.roomongit.dbnew.TodoFB
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor (private val database: TodoDatabase) {
    private val executor = Executors.newSingleThreadExecutor()
    fun getAll() = database.todoDao().getAll()
    fun add(todo: TodoFB) {
        executor.execute { database.todoDao().add(todo) }
    }
    fun remove(todo: TodoFB){
        executor.execute { database.todoDao().delete(todo) }
    }
}

