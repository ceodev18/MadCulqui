package com.maddog05.madculqui.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header

interface CulquiService {
    fun generateToken(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<JsonObject>
}