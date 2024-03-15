package com.example.roomongit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.roomongit.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    //private lateinit var viewModel: PlaceViewModel
    private lateinit var coordinatesOfLviv: PlaceMap //= <PlaceMap>("49.842957, 24.031111", "Marker in Lviv")
    private lateinit var coordinatesOfTernopil: PlaceMap //= <PlaceMap>("49.553516,25.5947767", "Marker in Ternopil")
    private lateinit var coordinatesOfSambir: PlaceMap //= <PlaceMap>("49.5207147,23.2065501", "Marker in Sambir")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        viewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
//        viewModel = PlaceViewModel()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Lviv, Ukraine.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap = googleMap
        coordinatesOfLviv = PlaceMap("49.842957,24.031111", "Lviv")
//        coordinatesOfLviv.coordinatesOf = "49.842957,24.031111"
//        coordinatesOfLviv.title = "Lviv"
        coordinatesOfTernopil = PlaceMap("49.553516,25.5947767", "Ternopil")
//        coordinatesOfTernopil.coordinatesOf = "49.553516,25.5947767"
//        coordinatesOfTernopil.title = "Ternopil"
        coordinatesOfTernopil = PlaceMap("49.5207147,23.2065501", "Sambir")
//        coordinatesOfSambir.coordinatesOf = "49.5207147,23.2065501"
//        coordinatesOfSambir.title = "Sambir"
        var coor: LatLng = objToLatLng(coordinatesOfLviv.coordinatesOf)
        mMap.addMarker(MarkerOptions()
            .position(coor)
            .title(coordinatesOfLviv.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor,10F))
        coor = objToLatLng(coordinatesOfTernopil.coordinatesOf)
        mMap.addMarker(MarkerOptions()
            .position(coor)
            .title(coordinatesOfTernopil.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor,10F))
        coor = objToLatLng(coordinatesOfSambir.coordinatesOf)
        mMap.addMarker(MarkerOptions()
            .position(coor)
            .title(coordinatesOfSambir.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor,10F))


        binding.fabBack.setOnClickListener {
            //getMyRoutes()
            //getMyPlaces()
        }
    }
    private fun getMyRoutes() {
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
                            mMap.addPolyline(PolylineOptions().addAll(decodedPath))
                        } else Log.e("MAPS_ROUTES", "No routes")
                    }
                }
            }
        }
    }

    fun getMyPlaces() {
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
                        mMap.addMarker(MarkerOptions().position(coordinates))
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
                            mMap.addPolyline(PolylineOptions().addAll(decodedPath))
                        }
                    }
                }
            }
        }
    }
    companion object{
        fun objToLatLng(latlan: String): LatLng {
            val latlanNew = latlan.replace(",","@")
            val llReplacedParts: List<String> = latlanNew.split("@" )
            return LatLng(
                llReplacedParts[0].toDouble(),
                llReplacedParts[1].toDouble()
            )
        }
    }
}