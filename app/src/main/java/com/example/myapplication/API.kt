package com.example.myapplication

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface API {
    @GET
    fun getPersonas(@Url url:String): Response<PersonaModel>
}
