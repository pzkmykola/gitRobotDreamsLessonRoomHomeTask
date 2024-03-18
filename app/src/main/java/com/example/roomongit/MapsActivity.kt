package com.example.roomongit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.roomongit.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator


class MapsActivity : AppCompatActivity(), GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: PlaceViewModel

    private val locationOfLviv = PlaceFB("", "Lviv","49.842957,24.031111" ,"")
    private val locationOfTernopil = PlaceFB("","Ternopil","49.553516,25.5947767","" )
    private val locationOfSambir = PlaceFB("","Sambir","49.5207147,23.2065501", "")
    private var origin:PlaceFB? = null
    private var previousMarker:Marker? = null
    private var tappedList = mutableListOf<PlaceFB>() //mutabl
    private var destination:PlaceFB? = null//:eListOf<PlaceMap>()
    private var imageRequest: String = ""
    private var reqCompleted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        viewModel.uiState.observe(this) { uiState ->
            when (uiState) {
                is PlaceViewModel.UIState.Empty -> Unit
                is PlaceViewModel.UIState.Processing -> Unit
                is PlaceViewModel.UIState.Result -> {
                    updateMap(uiState.placeList)
                }
                is PlaceViewModel.UIState.InMap -> {
                    updateMap(uiState.placeList)
                }
                is PlaceViewModel.UIState.ImageMap -> {
                      imageRequest = uiState.req
                }
            }

        }

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
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        updateMarkers()

        binding.fabPlaces.setOnClickListener {
            val query:String = binding.queryId.text.toString()
            if(origin!= null) {
                binding.tapText.text = "Tapped to get places with ${query}"
                viewModel.getMyPlaces(mMap, origin!!,query)
                reqCompleted = true
            }else{
                binding.tapText.text = "Place origin point!!!"
            }
        }

        binding.fabRoutes.setOnClickListener {
            if((origin != null) && (destination != null)){
                binding.tapText.text = "Tapped to get routes"
                viewModel.getMyRoutes(mMap, origin!!, destination!!)
                reqCompleted = true
            } else {
                binding.tapText.text = "Place origin and destination points!!!"
            }

        }

        binding.mapImage.setOnClickListener {
            if (reqCompleted) {
                reqCompleted = false
                mMap.clear()
                updateMarkers()
            }
//
//            if(imageRequest != ""){
//                Glide.with(binding.mapImage.context)
//                    .load(imageRequest)
//                    .into(binding.mapImage)
//            }
//            imageRequest = ""
        }

        binding.fabExit.setOnClickListener {
            viewModel.resetMap(mMap)
            supportFragmentManager.beginTransaction()
                .replace(com.google.android.material.R.id.container, HomeListFragment())
                .commitNow()
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

        if(origin == null || (origin != null && destination != null)){
            if(tappedList.isNotEmpty()) {
                tappedList.forEach {
                    val coor = viewModel.toLatLng(it.location)//coordinatesOf)
                    if (marker.position == coor) {
                        origin = it
                        destination = null//???
                        if(it.urlImage != "") {
                            Glide.with(binding.mapImage.context)
                                .load(it.urlImage)
                                .into(binding.mapImage)
                        }
                        binding.mapImage
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
    
    private fun updateMap(placeList: List<PlaceFB>){
        if(placeList.isNotEmpty()) {
            tappedList = mutableListOf<PlaceFB>()
            placeList.forEach {
                tappedList.add(it)
            }
            viewModel.resetMap(mMap)
            updateMarkers()
        }
    }

    private fun updateMarkers(){
        if(tappedList.isNotEmpty()) {
            tappedList.forEach {
                val coor: LatLng = viewModel.setCoordinate(it)
                mMap.addMarker(
                    MarkerOptions()
                        .position(coor)
                        .title(viewModel.setTitle(it))
                )
            }
        } else{
            val coor1: LatLng = viewModel.setCoordinate(locationOfLviv)
            tappedList.add(locationOfLviv)
            mMap.addMarker(
                MarkerOptions()
                    .position(coor1)
                    .title(viewModel.setTitle(locationOfLviv))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor1, 8F))
            val coor2 = viewModel.setCoordinate(locationOfTernopil)
            tappedList.add(locationOfTernopil)
            mMap.addMarker(
                MarkerOptions()
                    .position(coor2)
                    .title(viewModel.setTitle(locationOfTernopil))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor2, 6F))
            val coor3 = viewModel.setCoordinate(locationOfSambir)
            tappedList.add(locationOfSambir)
            mMap.addMarker(
                MarkerOptions()
                    .position(coor3)
                    .title(viewModel.setTitle(locationOfSambir))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor3, 6F))
        }
    }
}