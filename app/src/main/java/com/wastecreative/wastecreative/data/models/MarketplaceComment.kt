package com.wastecreative.wastecreative.data.models

import com.google.gson.annotations.SerializedName

data class MarketplaceComment (

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("pengguna_id")
    val pengguna_id: Int,

    @field:SerializedName("userName")
    val userName: String?,

    @field:SerializedName("userPhoto")
    val userPhoto: String?,

    @field:SerializedName("komentar")
    val comment: String,

    @field:SerializedName("tgl")
    val date: String
)