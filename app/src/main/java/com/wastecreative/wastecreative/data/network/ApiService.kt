package com.wastecreative.wastecreative.data.network

import com.wastecreative.wastecreative.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("craft")
    suspend fun getCraftList(
        @Query("limit") size: Int,
    ): ResponseListCraft

    @GET("craft/{id}")
    suspend fun getCraftDetail(
        @Path("id") id: String
    ): ResponseCraftDetail

    @GET("craft")
    suspend fun getCrafts(
        @Query("page") page: Int,
        @Query("limit") size: Int,
    ): ResponseListCraft

    @GET("craft")
    suspend fun getSearcCraftList(
        @Query("search") query: String
    ): ResponseListCraft

    @Multipart
    @POST("craft")
    suspend fun uploadCraft(
        @Part file: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
        @Part("bahan[]") bahan: ArrayList<String>,
        @Part("alat[]") alat: ArrayList<String>,
        @Part("langkah[]") langkah: ArrayList<String>,
        @Part("video") video: RequestBody,
        @Part("pengguna_id") pengguna_id: RequestBody,
    ): UploadResponse

    @GET("marketplace")
    suspend fun getPostsList(
    ): ResponseListMarketplace

    @GET("marketplace/{id}")
    suspend fun getPostsDetail(
        @Path("id") id: Int
    ): ResponseMarketplaceDetail

    @GET("marketplace")
    suspend fun getPosts(
        @Query("page") page: Int,
        @Query("limit") size: Int,
    ): ResponseListMarketplace
    @Multipart
    @POST("marketplace")
    suspend fun uploadMarketplace(
        @Part file: MultipartBody.Part,
        @Part("judul") title: RequestBody,
        @Part("deskripsi") desc: RequestBody,
        @Part("harga") price: RequestBody,
        @Part("alamat") address: RequestBody,
        @Part("pengguna_id") pengguna_id: RequestBody,
    ): UploadResponse
    @GET("marketplace-comment/{id}")
    suspend fun getCommentList(
        @Path("id") id: Int
    ): ResponseComment

    @Multipart
    @POST("marketplace-comment/")
    suspend fun postComment(
        @Part("komentar") comment: RequestBody,
        @Part("marketplace_id") marketplace_id: RequestBody,
        @Part("pengguna_id") user_id: RequestBody,
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