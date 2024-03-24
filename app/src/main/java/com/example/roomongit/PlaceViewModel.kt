package com.example.roomongit

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.PolyUtil
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceViewModel : ViewModel(), PlaceDao, PlaceMapDao {
    private var placeList:MutableList<PlaceFB> = mutableListOf()
    private val repo = MyApplication.getApp().repo
    private var _uiState = MutableLiveData<UIState>(UIState.Empty)
    val uiState: LiveData<UIState> = _uiState
    private var _gotoMap:Boolean = false
    init
    {
        getAll()
    }

    private fun getAll() {
        repo.database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //CoroutineScope(Dispatchers.IO).launch {
                _uiState.value = UIState.Processing
                placeList = mutableListOf()
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val taskKey: String = it.key!!
                        if (taskKey != "") {
                            val newItem = it.getValue(PlaceFB::class.java)
                            if (newItem != null && taskKey == newItem.id) {
                                Log.d(
                                    "MYRES1",
                                    "${newItem.id}/${newItem.title}/${newItem.location}/${newItem.urlImage}"
                                )
                                placeList.add(newItem)
                            }
                        }
                    }
                    if (placeList.isNotEmpty()) {
                            _uiState.postValue(UIState.Result(placeList))
                    }
                    _uiState.value = UIState.Empty
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun goToMap(){
        _gotoMap = true
        _uiState.postValue(UIState.Result(placeList))
    }

    fun resetMap(map: GoogleMap){
        if(map != null) {
            map.clear()
            _uiState.value = UIState.Empty
            _gotoMap = false
        }
    }
    override fun add(title: String, location: String, urlImage:String):Boolean{
        return repo.add(title = title , location = location, urlImage = urlImage)
    }
    override fun remove(place: PlaceFB){
        repo.remove(place)
    }

    override fun setCoordinate(placeMap: PlaceFB): LatLng {
        val coor:LatLng = toLatLng(placeMap.location)
        return coor
    }

    override fun setTitle(placeMap: PlaceFB): String {
        return placeMap.title
    }
    fun getMyRoutes(map: GoogleMap, origin:PlaceFB, destination:PlaceFB) {
        map.clear()
        val coor1: LatLng = setCoordinate(origin)
        map.addMarker(MarkerOptions()
            .position(coor1)
            .title(setTitle(origin)))
        val coor2: LatLng = setCoordinate(destination)
        map.addMarker(MarkerOptions()
            .position(coor2)
            .title(setTitle(destination)))
        CoroutineScope(Dispatchers.IO).launch {
            val result = Client.client.create(ApiInterface1::class.java)
                .getSimpleRoute(origin.location,destination.location)
            if (result.isSuccessful) {
                Log.d("APS_ROUTES", "Checked result")
                withContext(Dispatchers.Main) {
                    result.body()?.let {
                        if(it.routes.isNotEmpty()) {
                            val polylinePoints = it.routes[0].overviewPolyline.points
                            Log.d("MAPS_ROUTES", "Passed polylinePoints")
                            val decodedPath = PolyUtil.decode(polylinePoints)
                            Log.d("MAPS_MAPS_ROUTES", "Got decodedPath")
                            map.addPolyline(PolylineOptions().addAll(decodedPath))
                        } else Log.e("MAPS_ROUTES", "No routes")
                    }
                }
            }
        }
    }

    fun getMyPlaces(map: GoogleMap, placeMap: PlaceFB, queryType: String) {
        val str = "tourist_attractions"
        if(queryType.isEmpty()) {
            queryType.replaceFirst(queryType, str)
        }
        map.clear()
        val coor1: LatLng = setCoordinate(placeMap)
        map.addMarker(MarkerOptions()
            .position(coor1)
            .title(setTitle(placeMap)))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coor1,8F))
        CoroutineScope(Dispatchers.IO).launch {
            val result = Client.client.create(ApiInterface2::class.java)
                .getNearbyPlaces(placeMap.location,"1000",queryType,Keys.apiKey4)
            if (result.isSuccessful) {
                Log.d("MAPS_PLACES_QUERY", "Checked result for type $queryType")
                var locations = mutableListOf<Location>()
                result.body()?.let {
                    it.results.forEach { result ->
                        Log.d("MAPS_PLACES", "Result for each place")
                        val location = result.geometry.location
                        locations.add(location)
                    }
                }
                withContext(Dispatchers.Main) {
                    if(locations.isNotEmpty()) {
                        locations.forEach {
                            Log.d("MAPS_PLACES", "Result for each location")
                            val coordinates = LatLng(it.lat, it.lng)
                            map.addMarker(MarkerOptions().position(coordinates))
                        }
                        result.body()?.let {
                            val reference = it.results[0].photos[0].photoReference
                            val req =  "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photo_reference=$reference&key=Keys.apiKey4"
                            _uiState.value = UIState.ImageMap("")
                            _uiState.postValue(UIState.ImageMap(req))
                            _uiState.value = UIState.Empty
                        }
                    }
                }
            }
            withContext(Dispatchers.IO) {
                Log.d("MAPS_PLACES_ROUTES", "Start routes")
                val placeCoordinates = mutableListOf<String>()
                result.body()?.let { result ->
                    result.results.forEach {
                        placeCoordinates.add("${it.geometry.location.lat},${it.geometry.location.lng}")
                    }
                }
                val waypointCoordinates = placeCoordinates.drop(0).take(8)
                val waypointCoordinatesString =
                    waypointCoordinates.joinToString(separator = "|")
                val routeResult = Client.client.create(ApiInterface3::class.java)
                    .getComplexRoute(
                        originId = placeCoordinates[0],
                        destinationId = placeCoordinates.last(),
                        waypoints = waypointCoordinatesString
                    )
                if (routeResult.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        routeResult.body()?.let {
                            val polylinePoints = it.routes[0].overviewPolyline.points
                            val decodedPath = PolyUtil.decode(polylinePoints)
                            map.addPolyline(PolylineOptions().addAll(decodedPath))
                        }
                    }
                }
            }
        }
    }

    fun toLatLng(latlan: String): LatLng {
        val latlanNew = latlan.replace(",","@")
        val llReplacedParts: List<String> = latlanNew.split("@" )
        return LatLng(
            llReplacedParts[0].toDouble(),
            llReplacedParts[1].toDouble()
        )
    }

    sealed class UIState {
        object Empty : UIState()
        object Processing : UIState()
        class Result(val placeList: List<PlaceFB>) : UIState()
        class ImageMap(val req: String) : UIState()
    }
}

