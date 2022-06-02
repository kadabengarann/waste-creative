package com.wastecreative.wastecreative.data.network

import com.wastecreative.wastecreative.data.models.Craft
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("craft")
    suspend fun getCraftList(
    ): List<Craft>

    @GET("craft/{id}")
    suspend fun getCraftDetail(
        @Path("id") id: String
    ): Craft

    @GET("craft")
    suspend fun getCrafts(
        @Query("page") page: Int,
        @Query("limit") size: Int,
    ): List<Craft>
}