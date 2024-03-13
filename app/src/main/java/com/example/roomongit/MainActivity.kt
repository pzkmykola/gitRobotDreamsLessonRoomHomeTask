package com.example.roomongit


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.common.collect.Maps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity(), OnAuthLaunch,
    OnAddClickListener, OnSetMapClickListener, OnMapReadyCallback {

    private lateinit var supportMapFragment : SupportMapFragment
    private lateinit var myMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportMapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, supportMapFragment)
            .commit()
    }
    override fun launch(intent: Intent) {
        startActivityForResult(intent, 1)
    }
    override fun showListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(com.google.android.material.R.id.container, HomeListFragment())
            .commit()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val result = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(result.idToken, null)
                val auth = FirebaseAuth.getInstance()
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showListFragment()
                        } else{
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e:ApiException) {
                Toast.makeText(this, "Error $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFabClick() {
        supportFragmentManager.beginTransaction()
            .add(com.google.android.material.R.id.container, AddPlaceFragment())
            .addToBackStack("addPlaceFragment")
            .commit()
    }

    override fun onFabMapClick() {
        supportFragmentManager.beginTransaction()
            .add(com.google.android.material.R.id.container, SupportMapFragment())
            .addToBackStack("addSupportMapFragment")
            .commit()
//        supportMapFragment.getMapAsync { map ->
//            val coordinatesOfLviv = LatLng(49.842957, 24.031111)
//            map.addMarker(MarkerOptions().position(coordinatesOfLviv).title("My Position"))
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinatesOfLviv, 8F))}
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        val coordinatesOfLviv = LatLng(49.842957, 24.031111)
        myMap.addMarker(MarkerOptions().position(coordinatesOfLviv).title("My Position"))
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinatesOfLviv, 8F))
    }
}

interface OnAuthLaunch {
    fun launch(intent: Intent)
    fun showListFragment()
}

interface OnAddClickListener{
    fun onFabClick()
}

interface OnSetMapClickListener{
    fun onFabMapClick()
}