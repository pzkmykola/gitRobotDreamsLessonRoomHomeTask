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
    private lateinit var viewModel: PlaceViewModel
    private lateinit var coordinatesOfLviv: PlaceMap //= <PlaceMap>("49.842957, 24.031111", "Marker in Lviv")
    private lateinit var coordinatesOfTernopil: PlaceMap //= <PlaceMap>("49.553516,25.5947767", "Marker in Ternopil")
    private lateinit var coordinatesOfSambir: PlaceMap //= <PlaceMap>("49.5207147,23.2065501", "Marker in Sambir")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        viewModel = PlaceViewModel()
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
        coordinatesOfTernopil = PlaceMap("49.553516,25.5947767", "Ternopil")
        coordinatesOfSambir = PlaceMap("49.5207147,23.2065501", "Sambir")

        val coor1: LatLng = viewModel.setCoordinate(coordinatesOfLviv)
        mMap.addMarker(MarkerOptions()
            .position(coor1)
            .title(viewModel.setTitle(coordinatesOfLviv)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor1,8F))
//        val coor2 = viewModel.setCoordinate(coordinatesOfTernopil)
//        mMap.addMarker(MarkerOptions()
//            .position(coor2)
//            .title(viewModel.setTitle(coordinatesOfTernopil)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor2,6F))
//        val coor3 = viewModel.setCoordinate(coordinatesOfSambir)
//        mMap.addMarker(MarkerOptions()
//            .position(coor3)
//            .title(viewModel.setTitle(coordinatesOfSambir)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor3,6F))


        binding.fabBack.setOnClickListener {
            //viewModel.getMyRoutes(mMap)
            viewModel.getMyPlaces(mMap, coordinatesOfLviv)
        }
    }
}