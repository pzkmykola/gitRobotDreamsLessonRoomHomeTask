package com.example.roomongit.dbnew

//
data class PlaceFB (
    var id: String = "",
    var title: String = "",
    var location: String = "",
    var urlImage: String = ""
)

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
