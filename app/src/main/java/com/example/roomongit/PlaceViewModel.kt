package com.example.roomongit

import androidx.lifecycle.ViewModel

class PlaceViewModel : ViewModel(), PlaceDao {
    private val repo = MyApplication.getApp().repo
//    private val _listState = MutableLiveData<ListState>(ListState.EmptyList)
//    val listState: LiveData<ListState> = _listState
//    private val observer = Observer<List<TodoFB>> {
//        _listState.postValue(ListState.UpdatedList(list = it))
//    }

    override fun add(title: String, location: String, urlImage:String):Boolean{
        return repo.add(title = title , location = location, urlImage = urlImage)
    }
    override fun remove(place: PlaceFB){
        repo.remove(place)
    }

//    sealed class ListState {
//        object EmptyList:ListState()
//        class UpdatedList(val list:List<TodoFB>):ListState()
//    }
}