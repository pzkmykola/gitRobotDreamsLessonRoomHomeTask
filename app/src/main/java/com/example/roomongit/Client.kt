package com.example.roomongit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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
    @GET("/maps/api/place/nearbysearch/json?")
    suspend fun getNearbyPlaces(
        @Query("location") locationId: String,
        @Query("radius") radiusId: String,
        @Query("type") typeId: String,
        @Query("key") key: String = Keys.apiKey4
    ): retrofit2.Response<PlacesResponse>
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
