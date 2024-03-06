package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.roomongit.dbnew.TodoFB
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoViewModel  @Inject constructor (private var repo:TodoRepository): ViewModel() {
    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
    val listState: LiveData<ListState> = _listState
    private val observer = Observer<List<TodoFB>> {
        _listState.postValue(ListState.UpdatedList(list = it))
    }
    init {
        repo.getAll().observeForever(observer)
    }
    fun addTodo(title:String, note:String, date:String, completed:Boolean){
        repo.add(TodoFB(title = title, note = note, date = date, completed = completed))
    }
    fun removeTodo(todo: TodoFB){
        repo.remove(todo)
    }
    override fun onCleared() {
        repo.getAll().removeObserver(observer)
        super.onCleared()
    }
    sealed class ListState {
        object EmptyList:ListState()
        class UpdatedList(val list:List<TodoFB>):ListState()
    }
}