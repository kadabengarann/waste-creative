package com.wastecreative.wastecreative.data.network

import com.wastecreative.wastecreative.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("craft")
    suspend fun uploadCraft(
        @Part file: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
        @Part("bahan") bahan: RequestBody,
        @Part("alat") alat: RequestBody,
        @Part("langkah[]") langkah: ArrayList<String>,
        @Part("video") video: RequestBody,
        @Part("pengguna_id") pengguna_id: RequestBody,
    ): UploadResponse

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