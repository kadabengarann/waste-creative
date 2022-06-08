package com.wastecreative.wastecreative.data.models.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Craft (
    val id: String,

    val userName: String?,
    val userPhoto: String?,

    val name: String,
    val photo: String,

    val description: String,
): Parcelable