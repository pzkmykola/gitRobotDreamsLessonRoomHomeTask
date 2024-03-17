package com.example.roomongit

import com.google.android.gms.common.api.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object Client {
    val client: Retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

interface ApiInterface1 {
    @GET("/maps/api/directions/json?")
    suspend fun getSimpleRoute(
        @Query("origin") originId: String,
        @Query("destination") destinationId: String,
        @Query("key") key: String = Keys.apiKey4
    ): retrofit2.Response<DirectionsResponse>
}
interface ApiInterface2 {
    @GET("/maps/api/place/nearbysearch/json?location=49.842957,24.031111&radius=2000&type=tourist_attractions&key=${Keys.apiKey4}")
    suspend fun getNearbyPlaces(): retrofit2.Response<PlacesResponse>
}
interface ApiInterface3 {
    @GET("/maps/api/directions/json?")
    suspend fun getComplexRoute(
        @Query("origin") originId: String,
        @Query("destination") destinationId: String,
        @Query("waypoints") waypoints: String,
        @Query("key") key: String = Keys.apiKey4
    ): retrofit2.Response<DirectionsResponse>
}
