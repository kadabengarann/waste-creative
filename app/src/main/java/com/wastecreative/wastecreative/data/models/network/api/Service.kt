package com.wastecreative.wastecreative.data.models.network.api

import com.wastecreative.wastecreative.data.models.Response
import com.wastecreative.wastecreative.data.models.ResponseItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {

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




