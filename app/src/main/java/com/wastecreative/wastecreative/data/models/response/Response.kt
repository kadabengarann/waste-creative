package com.wastecreative.wastecreative.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Response(

	@field:SerializedName("Response")
	val response: List<ResponseItem>,

)
@Parcelize
data class ResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	var password: String,

	@field:SerializedName("avatar")
	var avatar: String,

	@field:SerializedName("id")
	var id: String,

	@field:SerializedName("email")
	var email: String,

	@field:SerializedName("username")
	var username: String,

	var isLogin: Boolean

) : Parcelable
