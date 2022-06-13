package com.wastecreative.wastecreative.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marketplace (

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("pengguna_id")
    val pengguna_id: Int,

    @field:SerializedName("userName")
    val userName: String?,

    @field:SerializedName("userPhoto")
    val userPhoto: String?,

    @field:SerializedName("like")
    val like: Int,

    @field:SerializedName("judul")
    val judul: String,

    @field:SerializedName("foto")
    var foto: String,

    @field:SerializedName("komentar")
    var komentar: Int,

    @field:SerializedName("tgl")
    var date: String,

): Parcelable