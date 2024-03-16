package com.example.roomongit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
//
data class PlaceFB (
    var id: String = "",
    var title: String = "",
    var location: String = "",
    var urlImage: String = ""
)

object Keys{
    const val apiKeyMain = "AIzaSyBW2AHurFTdDwh1cPBrYNOzs1vTRlMOb2M"
    const val apiKey4 = "AIzaSyBcPiQgySlkhfINlqFMzQoakW7B95o3kqE"
}
data class DirectionsResponse(val routes:List<Routes>)
data class Routes(@SerializedName("overview_polyline" ) val overviewPolyline:OverviewPolyline)
data class OverviewPolyline(val points:String)
data class PlacesResponse(val results:List<Results>)
data class Results(val geometry:Geometry, val photos:List<Photos>, val name:String)
data class Geometry(val location:Location)
data class Location(val lat:Double, val lng:Double)
data class Photos (@SerializedName("photo_reference") val photoReference:String? = null)

interface PlaceDao {
    fun add(title: String, location: String, urlImage:String):Boolean
    fun remove(place: PlaceFB)
}

interface PlaceMapDao{
    fun setCoordinate(placeMap: PlaceFB) : LatLng
    fun setTitle(placeMap: PlaceFB) : String
}
//abstract class TodoDatabase : RoomDatabase(){
//    abstract fun placeDao():PlaceDao
//}
