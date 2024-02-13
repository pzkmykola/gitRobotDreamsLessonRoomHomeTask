package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.room.InvalidationTracker
import com.example.roomongit.db.Employee

class EmployeeViewModel: ViewModel() {
    private val repo = MyApplication.getApp().repo
    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
    val listState: LiveData<ListState> = _listState
    private val observer = Observer<List<Employee>> {
        _listState.postValue(ListState.UpdatedList(list = it))
    }
    init {
        repo.getAll().observeForever(observer)
    }
    fun addEmployee(name:String, position:String){
        repo.add(Employee(name = name, position = position))
    }
    fun removeEmployee(employee: Employee){
        repo.remove(employee)
    }
    override fun onCleared() {
        repo.getAll().removeObserver(observer)
        super.onCleared()
    }
    sealed class ListState {
        object EmptyList:ListState()
        class UpdatedList(val list:List<Employee>):ListState()
    }
}