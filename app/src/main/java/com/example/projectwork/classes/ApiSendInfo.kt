package com.example.projectwork.classes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSendInfo {
    @POST("/api/accounts")
    fun createAccount(@Body account: CAccount): Call<Void>

}