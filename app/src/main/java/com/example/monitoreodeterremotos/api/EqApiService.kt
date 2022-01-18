package com.example.monitoreodeterremotos.api

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//Para utilizar el internet se necesita solicitar el permiso en manifest <uses-permission android:name="android.permission.INTERNET"/>
interface EqApiService {
    //URL llamada
        @GET("all_hour.geojson")
    //Este metodo se llama dentro de una currutina en MainViewModel
    suspend fun getUltimoTerremoto(): EqJsonResponse
}

//Donde se hace la solicitud
private var retrofit = Retrofit.Builder()
    .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
        //convierte los datos en tipo string, se debe implementar  implementation 'com.squareup.retrofit2:converter-scalars:2.5.0'
        //Moshi convierte directamente en un objeto JOSON implementation 'com.squareup.retrofit2:converter-moshi:2.5.0'
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

var service: EqApiService = retrofit.create<EqApiService>(EqApiService::class.java)