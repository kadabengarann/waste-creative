package com.wastecreative.wastecreative.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class CraftDetail  (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("pengguna_id")
    val user_id: Int,

    @field:SerializedName("userName")
    val userName: String?,

    @field:SerializedName("userPhoto")
    val userPhoto: String?,

    @field:SerializedName("nama")
    val name: String,

    @field:SerializedName("foto")
    var photo: String,

    @field:SerializedName("alat")
    val tools: List<String>,

    @field:SerializedName("bahan")
    val materials: List<String>,

    @field:SerializedName("langkah")
    val steps: List<String>,

    @field:SerializedName("video")
    val video: String
    ): Parcelable