package com.example.projectwork.classes

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiSendInfo {
    @POST("/api/accounts")
    fun createAccount(@Body account: CAccount): Call<Void>

    @GET("profiles/{profileId}")
    fun getProfile(@Path("profileId") profileId: Int): Call<CAccount>

    @GET("api/accounts")
    fun getNearbyAccounts(): Call<List<CAccount>>

    @POST("api/send_token")
    fun sendToken(@Header("Authorization") token: String): Call<ResponseBody>


}