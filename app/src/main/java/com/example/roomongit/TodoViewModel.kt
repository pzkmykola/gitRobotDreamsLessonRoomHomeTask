package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.roomongit.dbnew.TodoFB

class TodoViewModel : ViewModel() {
    private val repo = MyApplication.getApp().repo
    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
    val listState: LiveData<ListState> = _listState
    private val observer = Observer<List<TodoFB>> {
        _listState.postValue(ListState.UpdatedList(list = it))
    }

    init {
        repo.getAll()
    }
    fun add(title: String, note: String, date:String):Boolean{
        return repo.add(title = title , note = note, date = date)
    }
    fun remove(todo: TodoFB){
        repo.remove(todo)
    }

    sealed class ListState {
        object EmptyList:ListState()
        class UpdatedList(val list:List<TodoFB>):ListState()
    }
}