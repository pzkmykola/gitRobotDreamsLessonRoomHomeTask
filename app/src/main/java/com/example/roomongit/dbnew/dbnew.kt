package com.example.roomongit.dbnew

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.example.roomongit.util.UiState
import com.google.firebase.firestore.DocumentId

import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoFB (
    var id:String  = "",
    var title:String = "", // the title of the task
    var note: String = "", // short description and priority: Low, Medium, High
    var date: String = "", // 2024-03-04
    var completed: Boolean = false // true or false
    ) : Parcelable

/*data class User(
    var id: String = "", // the id of the user
    val name: String = "", // the name of the user
    val email: String = "", // the email of the user
)*/
interface TodoRepository {
    fun addTodo(todo: TodoFB, result: (UiState<Pair<TodoFB, String>>) -> Unit)
    fun updateTodo(todo: TodoFB, result: (UiState<Pair<TodoFB,String>>) -> Unit)
    fun deleteTodo(todo: TodoFB, result: (UiState<Pair<TodoFB,String>>) -> Unit)
    fun getTodo(id: String, result: (UiState<Pair<TodoFB,String>>) -> Unit)
    fun getTodos(result: (UiState<List<TodoFB>>) -> Unit)
    fun storeTodos(todos: List<TodoFB>, result: (UiState<String>) -> Unit)
}
