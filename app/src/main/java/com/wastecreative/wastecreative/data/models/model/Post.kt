package com.wastecreative.wastecreative.data.models.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    val id: String,

    val userName: String?,
    val userPhoto: String?,

    val title: String,
    val photo: String,

    val description: String,
): Parcelable