package com.example.roomongit


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity(), OnAuthLaunch,
    OnAddClickListener {

//    private lateinit var supportMapFragment : SupportMapFragment
//    private lateinit var myMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
//            .addToBackStack("addPlaceFragment")
            .commit()
    }
}

interface OnAuthLaunch {
    fun launch(intent: Intent)
    fun showListFragment()
}

interface OnAddClickListener{
    fun onFabClick()
}

