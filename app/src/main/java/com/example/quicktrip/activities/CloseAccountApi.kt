package com.example.quicktrip.activities

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CloseAccountApi {
    @POST("closeAccount")
    fun closeAccount(@Body request: CloseAccountRequest): Call<ApiResponse>
}