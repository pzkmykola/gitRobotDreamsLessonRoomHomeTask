package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.roomongit.dbnew.TodoNew

class TodoViewModel: ViewModel() {
    private val repo = MyApplication.getApp().repo
    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
    val listState: LiveData<ListState> = _listState
    private val observer = Observer<List<TodoNew>> {
        _listState.postValue(ListState.UpdatedList(list = it))
    }
    init {
        repo.getAll().observeForever(observer)
    }
    fun addTodo(title:String, note:String, date:String){
        repo.add(TodoNew(title = title, note = note, date = date))
    }
    fun removeTodo(todo: TodoNew){
        repo.remove(todo)
    }
    override fun onCleared() {
        repo.getAll().removeObserver(observer)
        super.onCleared()
    }
    sealed class ListState {
        object EmptyList:ListState()
        class UpdatedList(val list:List<TodoNew>):ListState()
    }
}