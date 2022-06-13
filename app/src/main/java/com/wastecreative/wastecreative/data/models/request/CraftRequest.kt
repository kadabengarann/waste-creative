package com.wastecreative.wastecreative.data.models.request

data class CraftRequest (
        var name: String?,
        var tools: ArrayList<String>,
        var materials: ArrayList<String>,
        var steps: ArrayList<String>,
        var video: String?
)