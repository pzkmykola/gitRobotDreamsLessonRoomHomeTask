package com.example.roomongit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater

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

    private lateinit var coordinatesOfLviv: LatLng
    private lateinit var coordinatesOfTernopil: LatLng
    private lateinit var coordinatesOfSambir: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.manu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        coordinatesOfTernopil = LatLng(49.553516, 25.594767)
        coordinatesOfLviv = LatLng(49.842957, 24.031111)
        coordinatesOfSambir = LatLng(49.5207147,23.2065501)

        mMap.addMarker(MarkerOptions().position(coordinatesOfLviv).title("Marker in Lviv"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinatesOfLviv,10F))

        mMap.addMarker(MarkerOptions().position(coordinatesOfTernopil).title("Marker in Ternopil"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinatesOfTernopil, 10F))

        mMap.addMarker(MarkerOptions().position(coordinatesOfSambir).title("Marker in Sambir"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinatesOfSambir, 10F))

        binding.fabBack.setOnClickListener {
            getMyRoutes()
        }
//            supportFragmentManager.beginTransaction()
//                .replace(com.google.android.material.R.id.container, HomeListFragment())
//                //.addToBackStack("homeListFragment")
//                .commit()
//        }
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
}