package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.roomongit.dbnew.TodoDatabase_Impl
import com.example.roomongit.dbnew.TodoFB
import com.example.roomongit.dbnew.TodoRepository
import com.example.roomongit.util.UiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoViewModel  @Inject constructor (
    val repository: TodoRepository
): ViewModel() {
    private val TAG = "todoViewModel"
    private val _addTodo = MutableLiveData<UiState<Pair<TodoFB, String>>>()
    val addTodo: LiveData<UiState<Pair<TodoFB,String>>>
        get() = _addTodo

    private val _updateTodo = MutableLiveData<UiState<Pair<TodoFB,String>>>()
    val updateTodo: LiveData<UiState<Pair<TodoFB,String>>>
        get() = _updateTodo

    private val _doneTodo = MutableLiveData<UiState<Pair<TodoFB,String>>>()
    val doneTodo: LiveData<UiState<Pair<TodoFB,String>>>
        get() = _doneTodo

    private val _deleteTodo = MutableLiveData<UiState<Pair<TodoFB,String>>>()
    val deleteTodo: LiveData<UiState<Pair<TodoFB,String>>>
        get() = _deleteTodo

    private val _storeTodos = MutableLiveData<UiState<String>>()
    val storeTodos: LiveData<UiState<String>>
        get() = _storeTodos

    private val _getTodo = MutableLiveData<UiState<Pair<TodoFB,String>>>()
    val getTodo: LiveData<UiState<Pair<TodoFB,String>>>
        get() = _getTodo

    private val _getTodos = MutableLiveData<UiState<List<TodoFB>>>()
    val getTodos: MutableLiveData<UiState<List<TodoFB>>>
        get() = _getTodos

    private val _todos = MutableLiveData<UiState<List<TodoFB>>>()
    val todos: LiveData<UiState<List<TodoFB>>>
        get() = _todos

    fun addTodo(todo: TodoFB) {
        _addTodo.value = UiState.Loading
        repository.addTodo(todo) {
            _addTodo.value = it
        }
    }

    fun updateTodo(todo: TodoFB) {
        _updateTodo.value = UiState.Loading
        repository.updateTodo(todo) {
            _updateTodo.value = it
        }
    }

    fun doneTodo(todo: TodoFB) {
        _doneTodo.value = UiState.Loading
        todo.completed = !todo.completed
        repository.updateTodo(todo) {
            _doneTodo.value = it
        }
    }

    fun deleteTodo(todo: TodoFB) {
        _deleteTodo.value = UiState.Loading
        repository.deleteTodo(todo) {
            _deleteTodo.value = it
        }
    }

    fun getTodo(id: String) {
        _getTodo.value = UiState.Loading
        repository.getTodo(id) {
            _getTodo.value = it
        }
    }

    fun getTodos() {
        _todos.value = UiState.Loading
        repository.getTodos {
            _todos.value = it
        }
    }

    fun storeTodos(todos: List<TodoFB>) {
        _storeTodos.value = UiState.Loading
        repository.storeTodos(todos) {
            _storeTodos.value = it
        }
    }
}