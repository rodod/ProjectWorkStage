package com.example.projectwork.classes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiSendInfo {
    @POST("/api/accounts")
    @Headers("Authorization: Bearer {token}")
    fun createAccount(@Body account: CAccount): Call<Void>

    @GET("profiles/{profileId}")
    @Headers("Authorization: Bearer {token}")
    fun getProfile(@Path("profileId") profileId: Int): Call<CAccount>

    @GET("api/accounts")
    @Headers("Authorization: Bearer {token}")
    fun getNearbyAccounts(): Call<List<CAccount>>

    @POST("api/password")
    @Headers("Authorization: Bearer {token}")
    fun sendNewPassword(@Body password : String) : Call<Void>

    @POST("api/token")
    fun sendToken(@Body token : String) : Call<Void>
}

fun createRetrofitInstance(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
