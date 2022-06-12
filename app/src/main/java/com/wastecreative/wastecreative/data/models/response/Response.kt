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
data class UploadResponse(
	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
data class ResponseListCraft(

	@field:SerializedName("data")
	val data: List<Craft>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String

)

data class ResponseCraftDetail(

	@field:SerializedName("data")
	val data: CraftDetail,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String

)
data class ResponseListMarketplace(

	@field:SerializedName("data")
	val data: List<Marketplace>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String

)
data class ResponseComment(

	@field:SerializedName("comments")
	val comments: List<MarketplaceComment>,

	@field:SerializedName("id")
	val id: Int

)
data class ResponseMarketplaceDetail(

	@field:SerializedName("data")
	val data: MarketplaceDetail,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String

)