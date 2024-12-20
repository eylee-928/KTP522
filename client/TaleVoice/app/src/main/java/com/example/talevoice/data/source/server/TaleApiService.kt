package com.example.talevoice.data.source.server

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TaleApiService {

    @GET("api/tales/list")
    suspend fun getTaleList(): Response<NetworkTaleList>

    @GET("api/tales/content/{id}")
    suspend fun getTaleDetail(@Path("id") taleId: String): Response<NetworkTaleContent>
}