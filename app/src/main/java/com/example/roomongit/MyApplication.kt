package com.example.roomongit

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyApplication: Application() {
    lateinit var target:DatabaseReference
    lateinit var repo:PlaceRepository

    override fun onCreate() {
        super.onCreate()
        instance = this

        val database = FirebaseDatabase.getInstance(
            "https://roomongit-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        val account = GoogleSignIn.getLastSignedInAccount(this)

        target = database.reference
            .child(account?.id ?: "unknown_account").child("Places")

        repo = PlaceRepository(target)
    }
    companion object {
        private lateinit var instance:MyApplication
        fun getApp() = instance
    }
}