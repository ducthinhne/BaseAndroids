package com.example.baseandroid.data.network

import okhttp3.ResponseBody
import retrofit2.http.*

typealias Parameters = HashMap<String, Any>
typealias Headers = HashMap<String, String>

interface ApiService {

    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @QueryMap parameters: Parameters = hashMapOf()
    ): ResponseBody

    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @Body parameters: Parameters = hashMapOf()
    ): ResponseBody

    @PUT
    suspend fun put(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @Body parameters: Parameters = hashMapOf()
    ): ResponseBody

    @DELETE
    suspend fun delete(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @QueryMap parameters: Parameters = hashMapOf()
    ): ResponseBody
}
