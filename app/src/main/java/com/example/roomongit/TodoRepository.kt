package com.example.roomongit

import com.example.roomongit.dbnew.TodoDatabase
import com.example.roomongit.dbnew.TodoNew
import java.util.concurrent.Executors

class TodoRepository(private val database: TodoDatabase) {
    private val executor = Executors.newSingleThreadExecutor()
    fun getAll() = database.todoDao().getAll()
    fun add(todo: TodoNew) {
        executor.execute { database.todoDao().add(todo) }
    }
    fun remove(todo: TodoNew){
        executor.execute { database.todoDao().delete(todo) }
    }
}

