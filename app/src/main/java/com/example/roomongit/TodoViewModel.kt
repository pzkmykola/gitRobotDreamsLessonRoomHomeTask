package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.roomongit.dbnew.TodoDao
import com.example.roomongit.dbnew.TodoFB

class TodoViewModel : ViewModel(), TodoDao {
    private val repo = MyApplication.getApp().repo
    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
    val listState: LiveData<ListState> = _listState
    private val observer = Observer<List<TodoFB>> {
        _listState.postValue(ListState.UpdatedList(list = it))
    }

    init {
        this.getAll().observeForever(observer)
    }
    override fun add(title: String, note: String, date:String):Boolean{
        return repo.add(title = title , note = note, date = date)
    }
    override fun remove(todo: TodoFB){
        repo.remove(todo)
    }

    override fun getAll(): LiveData<List<TodoFB>>{
        return repo.getAll() as LiveData<List<TodoFB>>
    }

    sealed class ListState {
        object EmptyList:ListState()
        class UpdatedList(val list:List<TodoFB>):ListState()
    }
}