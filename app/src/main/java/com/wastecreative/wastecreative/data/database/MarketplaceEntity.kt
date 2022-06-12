package com.wastecreative.wastecreative.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "marketplace")
@Parcelize
data class MarketplaceEntity(

    @PrimaryKey
    val id: String,

    val pengguna_id: Int,

    val userName: String?,

    val userPhoto: String?,

    val like: Int,

    val judul: String,

    var foto: String,

    var komentar: Int,

) : Parcelable