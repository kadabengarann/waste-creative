package com.wastecreative.wastecreative.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class CraftDetail  (

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

    @field:SerializedName("tools")
    val tools: List<String>,

    @field:SerializedName("materials")
    val materials: List<String>,

    @field:SerializedName("steps")
    val steps: List<String>
    ): Parcelable