package com.example.roomongit

//import com.google.gson.annotations.SerializedName
//
data class PlaceFB (
    var id: String = "",
    var title: String = "",
    var location: String = "",
    var urlImage: String = ""
)
//data class DirectionsResponse(val routes:List<Routes>)
//data class Routes(@SerializedName("overview_polyline" ) val overviewPolyline:OverviewPolyline)
//data class OverviewPolyline(val points:String)
//@Dao
interface PlaceDao {
    //@Insert
    fun add(title: String, location: String, urlImage:String):Boolean
    //@Delete
    fun remove(place: PlaceFB)
}

//abstract class TodoDatabase : RoomDatabase(){
//    abstract fun placeDao():PlaceDao
//}
