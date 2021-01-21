package com.geek.mvvm.network

import com.geek.mvvm.data.model.LoggedInUser
import com.geek.mvvm.data.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://private-222d3-homework5.apiary-mock.com/"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface GithubApiService {
    @GET("/users/{profile}")
    fun getUserData(@Path("profile") profile: String):
            Call<GithubUser>

    @POST("api/login")
    fun login(
        @Header("Content-Type") content_type: String?,
        @Header("IMSI") IMSI: String?,
        @Header("IMEI") IMEI: String?,
        @Body login: LoggedInUser?
    ): Call<LoginResponse>
}

object GithubApi {
    val retrofitService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }
}