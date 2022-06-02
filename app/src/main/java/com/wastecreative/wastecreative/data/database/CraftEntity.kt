package com.wastecreative.wastecreative.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "craft")
@Parcelize
data class CraftEntity(

    @PrimaryKey
    val id: String,

    val userName: String?,

    val userPhoto: String?,

    val createdAt: String,

    val like: Int,

    val name: String,

    val description: String,

    var photo: String,
) : Parcelable