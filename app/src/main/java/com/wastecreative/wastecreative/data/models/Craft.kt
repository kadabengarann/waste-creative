package com.wastecreative.wastecreative.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Craft (

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("userName")
    val userName: String?,

    @field:SerializedName("userPhoto")
    val userPhoto: String?,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("like")
    val like: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photo")
    var photo: String,

): Parcelable