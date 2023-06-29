package com.example.projectwork.classes

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import kotlin.coroutines.resumeWithException

interface ApiSendInfo {
    @POST("/api/accounts")
    fun createAccount(@Body account: CAccount): Call<Void>

    @GET("profiles/{profileId}")
    fun getProfile(@Path("profileId") profileId: Int): Call<CAccount>

    @GET("api/accounts")
    fun getNearbyAccounts(): Call<List<CAccount>>

    @PUT("api/password")
    fun sendNewPassword(@Body password : String) : Call<Void>

    @POST("api/token")
    fun sendLogin(@Body email : String, password: String) : Call<Void>

    @GET("message/{id}")
    fun getMessages(): Call<List<CMessage>>
}

fun createRetrofitInstance(): Retrofit {
    val tokenInterceptor = TokenInterceptor()

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl("link")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { getToken() }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }

    private suspend fun getToken(): String = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val user = FirebaseAuth.getInstance().currentUser
            val tokenTask = user?.getIdToken(false)
            val tokenResult = tokenTask?.result?.token

            if (tokenResult != null) {
                continuation.resume(tokenResult)
            } else {
                val exception = tokenTask?.exception ?: Exception("Failed to get token")
                continuation.resumeWithException(exception)
            }
        }
    }
}

private fun <T> CancellableContinuation<T>.resume(value: T) {
    resume(value)
}


