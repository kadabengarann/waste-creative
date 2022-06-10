package com.wastecreative.wastecreative.data.models

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("Response")
	val response: List<ResponseItem>,

)

data class ResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String

)
