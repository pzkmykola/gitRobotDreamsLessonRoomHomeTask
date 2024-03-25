package com.example.roomongit

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.roomongit.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.roomongit.PermissionUtils.isPermissionGranted
import com.example.roomongit.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MapsActivity : AppCompatActivity(), GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    companion object {
        private const val MAP_BUNDLE_KEY = "map_bundle_key"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: PlaceViewModel
    private lateinit var mapFragment:SupportMapFragment
    private val locationOfLviv = PlaceFB("", "Lviv","49.842957,24.031111" ,"")
    private val locationOfTernopil = PlaceFB("","Ternopil","49.553516,25.5947767","" )
    private val locationOfSambir = PlaceFB("","Sambir","49.5207147,23.2065501", "")
    private var origin:PlaceFB? = null
    private var previousMarker:Marker? = null
    private var tappedList = mutableListOf<PlaceFB>()
    private var destination:PlaceFB? = null
    private var imageRequest: String = ""
    private var reqCompleted = false
    private lateinit var persistedMapBundle: Bundle
    private var permissionDenied = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(MAP_BUNDLE_KEY, persistedMapBundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        persistedMapBundle = savedInstanceState?.getBundle(MAP_BUNDLE_KEY) ?: Bundle()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
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
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        viewModel.uiState.observe(this) { uiState ->
            Log.d("MAPS:", "View model state ${uiState.toString()}")
            when (uiState) {
                is PlaceViewModel.UIState.Empty -> Unit
                is PlaceViewModel.UIState.Processing -> Unit
                is PlaceViewModel.UIState.Result -> {
                    updateMap(uiState.placeList)
                }
                is PlaceViewModel.UIState.ImageMap -> {
                    imageRequest = uiState.req
                }
            }
        }
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        enableMyLocation()//new
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
            goBack()
        }
    }

    private fun goBack(){
        onBackPressedDispatcher.onBackPressed()
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
                        destination = null
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

    override fun onMyLocationClick(location: Location) {
        binding.tapText.text = "Current location:\n$location"
    }

    override fun onMyLocationButtonClick(): Boolean {
        var placeHolder:String = ""
        if(mMap.isMyLocationEnabled){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                binding.tapText.text = "Permission to get location denied"
                return false
            }
            CoroutineScope(Dispatchers.IO).launch{
                val result = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    CancellationTokenSource().token,).await()
                result?.let { location ->
                    if(location != null){
                        placeHolder = "MyLocation - " + "lat: " +location.latitude.toString() + " long: " + location.longitude.toString()
                        return@let
                    }
                }
            }
        }
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        if(placeHolder != "") binding.tapText.text = placeHolder
        return false
    }

    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        // Enable the my location layer if the permission has been granted.
        // Permission was denied. Display an error message
        // [START_EXCLUDE]
        // Display the missing permission error dialog when the fragments resume.
        // [END_EXCLUDE]
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION))
            enableMyLocation()
        else permissionDenied = true
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {

        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }

    // [END maps_check_location_permission_result]
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }
    private fun updateMap(placeList: List<PlaceFB>){
        if(mMap!= null) {
            if (placeList.isNotEmpty()) {
                tappedList = mutableListOf()
                placeList.forEach {
                    tappedList.add(it)
                }
                mMap.clear()
                updateMarkers()
            }
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