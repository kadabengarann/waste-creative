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

    @field:SerializedName("nama")
    val name: String,

    @field:SerializedName("foto")
    var photo: String,

): Parcelable