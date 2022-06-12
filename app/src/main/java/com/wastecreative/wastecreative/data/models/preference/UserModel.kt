package com.wastecreative.wastecreative.data.models.preference

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var id : String,
    var name: String,
    var email:String,
    @field:SerializedName("avatar")
    var img:String,
    var password:String,
    var isLogin: Boolean

    ) : Parcelable