package com.wastecreative.wastecreative.data.network

import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.models.Response
import com.wastecreative.wastecreative.data.models.ResponseItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("craft")
    suspend fun getCraftList(
    ): List<Craft>

    @GET("craft/{id}")
    suspend fun getCraftDetail(
        @Path("id") id: String
    ): CraftDetail

    @GET("craft")
    suspend fun getCrafts(
        @Query("page") page: Int,
        @Query("limit") size: Int,
    ): List<Craft>

    @GET("craft")
    suspend fun getSearcCraftList(
        @Query("search") query: String
    ): List<Craft>

    @FormUrlEncoded
    @POST("users")
    fun getLogin(
        @Field("email") email : String,
        @Field("password") password: String
    ): Call<Response>

    @FormUrlEncoded
    @POST("users")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email : String,
        @Field("password") password: String
    ): Call<ResponseItem>
}