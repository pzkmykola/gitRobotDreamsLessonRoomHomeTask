package com.example.roomongit

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class PlaceViewModel : ViewModel(), PlaceDao {
    private val repo = MyApplication.getApp().repo

    override fun add(title: String, location: String, urlImage:String):Boolean{
        return repo.add(title = title , location = location, urlImage = urlImage)
    }
    override fun remove(place: PlaceFB){
        repo.remove(place)
    }

    override fun setCoordinate(placeMap: PlaceMap): String {
        return placeMap.coordinatesOf
    }

    override fun setTitle(placeMap: PlaceMap): String {
        return placeMap.title
    }

    fun toLatLng(latlan: String): LatLng {
        val llReplacedParts: List<String> = latlan.split(", ")
        return LatLng(
            llReplacedParts[0].toDouble(),
            llReplacedParts[1].toDouble()
        )
    }

//    companion object{
//        fun objToLatLng(latlan: String): LatLng {
//            val llReplacedParts: List<String> = latlan.split(", ")
//            return LatLng(
//                llReplacedParts[0].toDouble(),
//                llReplacedParts[1].toDouble()
//            )
//        }
//    }

}