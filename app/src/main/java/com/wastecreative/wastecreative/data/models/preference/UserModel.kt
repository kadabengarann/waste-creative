package com.wastecreative.wastecreative.data.models.preference

data class UserModel (
    var id: Int,
    val name: String,
    val email:String,
    val isLogin: Boolean,
    val avatar: String,

    )