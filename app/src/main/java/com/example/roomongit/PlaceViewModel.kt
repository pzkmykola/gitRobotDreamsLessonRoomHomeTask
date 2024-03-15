package com.example.roomongit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceViewModel : ViewModel(), PlaceDao {
    private val repo = MyApplication.getApp().repo

    override fun add(title: String, location: String, urlImage:String):Boolean{
        return repo.add(title = title , location = location, urlImage = urlImage)
    }
    override fun remove(place: PlaceFB){
        repo.remove(place)
    }

    override fun setCoordinate(placeMap: PlaceMap): LatLng {
        val coor:LatLng = toLatLng(placeMap.coordinatesOf)
        return coor
    }

    override fun setTitle(placeMap: PlaceMap): String {
        return placeMap.title
    }
    fun getMyRoutes(map: GoogleMap, origin:PlaceMap, destination:PlaceMap) {
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
            val result = Client.client.create(ApiInterface1::class.java).getSimpleRoute()
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

    fun getMyPlaces(map: GoogleMap, placeMap: PlaceMap) {
        map.clear()
        val coor1: LatLng = setCoordinate(placeMap)
        map.addMarker(MarkerOptions()
            .position(coor1)
            .title(setTitle(placeMap)))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coor1,8F))
        CoroutineScope(Dispatchers.IO).launch {
            val result = Client.client.create(ApiInterface2::class.java).getNearbyPlaces()
            if (result.isSuccessful) {
                Log.d("MAPS_PLACES", "Checked result")
                var locations = mutableListOf<Location>()
                result.body()?.let {
                    it.results.forEach { result ->
                        Log.d("MAPS_PLACES", "Result for each place")
                        val location = result.geometry.location
                        locations.add(location)
                    }
                }
                withContext(Dispatchers.Main) {
                    locations.forEach {
                        Log.d("MAPS_PLACES", "Result for each location")
                        val coordinates = LatLng(it.lat, it.lng)
                        map.addMarker(MarkerOptions().position(coordinates))
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
                val waypointCoordinates = placeCoordinates.drop(0).take(10)
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
}