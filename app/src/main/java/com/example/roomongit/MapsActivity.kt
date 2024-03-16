package com.example.roomongit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.roomongit.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: PlaceViewModel
    private lateinit var locationOfLviv: PlaceFB//("49.842957, 24.031111", "Marker in Lviv")
    private lateinit var locationOfTernopil: PlaceFB //("49.553516,25.5947767", "Marker in Ternopil")
    private lateinit var locationOfSambir: PlaceFB//("49.5207147,23.2065501", "Marker in Sambir")
    private var origin:PlaceFB? = null
    private var previousMarker:Marker? = null
    private var tappedList = mutableListOf<PlaceFB>() //mutabl
    private var destination:PlaceFB? = null//:eListOf<PlaceMap>()
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
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this);

        mMap = googleMap
        locationOfLviv = PlaceFB("", "Lviv","49.842957,24.031111" ,"")
        locationOfTernopil = PlaceFB("","Ternopil","49.553516,25.5947767","" )
        locationOfSambir = PlaceFB("","Sambir","49.5207147,23.2065501", "")

        val coor1: LatLng = viewModel.setCoordinate(locationOfLviv)
        tappedList.add(locationOfLviv)
        //origin = locationOfLviv
        mMap.addMarker(MarkerOptions()
            .position(coor1)
            .title(viewModel.setTitle(locationOfLviv)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor1,8F))
        val coor2 = viewModel.setCoordinate(locationOfTernopil)
        tappedList.add(locationOfTernopil)
        //destination = locationOfTernopil
        mMap.addMarker(MarkerOptions()
            .position(coor2)
            .title(viewModel.setTitle(locationOfTernopil)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor2,6F))
        val coor3 = viewModel.setCoordinate(locationOfSambir)
        tappedList.add(locationOfSambir)
        mMap.addMarker(MarkerOptions()
            .position(coor3)
            .title(viewModel.setTitle(locationOfSambir)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor3,6F))


        binding.fabPlaces.setOnClickListener {
            if(origin!= null) {
                binding.tapText.text = "Tapped to get places"
                viewModel.getMyPlaces(mMap, origin!!)
            }else{
                binding.tapText.text = "Place origin point!!!"
            }
        }

        binding.fabRoutes.setOnClickListener {
            if((origin != null) && (destination != null)){
                binding.tapText.text = "Tapped to get routes"
                viewModel.getMyRoutes(mMap, origin!!, destination!!)
            } else {
                binding.tapText.text = "Place origin and destination points!!!"
            }

        }
    }
    override fun onMapClick(point: LatLng) {
        binding.tapText.text = "tapped, point=$point"
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val locAddress = marker.title
        binding.tapText.text = "tapped $locAddress"

        if (previousMarker != null) {
            previousMarker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        previousMarker = marker

        if(origin == null){
            if(tappedList.isNotEmpty()) {
                tappedList.forEach {
                    val coor = viewModel.toLatLng(it.location)//coordinatesOf)
                    if (marker.position == coor) {
                        origin = it
                        binding.tapText.text = "Origin is defined to ${it.title}"
                        return@forEach
                    }
                }
            }
        } else if(destination == null){
            if(tappedList.isNotEmpty()) {
                tappedList.forEach {
                    val coor = viewModel.toLatLng(it.location)
                    if (marker.position == coor) {
                        destination = it
                        binding.tapText.text = "Destination is defined to ${it.title}"
                        return@forEach
                    }
                }
            }
        } else binding.tapText.text = "Origin  and Destination already defined!"

        return true
    }

}