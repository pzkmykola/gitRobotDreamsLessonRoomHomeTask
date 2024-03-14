package com.example.roomongit

import androidx.lifecycle.ViewModel

class PlaceViewModel : ViewModel(), PlaceDao {
    private val repo = MyApplication.getApp().repo

    override fun add(title: String, location: String, urlImage:String):Boolean{
        return repo.add(title = title , location = location, urlImage = urlImage)
    }
    override fun remove(place: PlaceFB){
        repo.remove(place)
    }
}