package com.wastecreative.wastecreative.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wastecreative.wastecreative.R


fun ImageView.loadImage(url: String?, radius: Int) {
    Glide.with(this.context)
        .load(url)
        .transform(MultiTransformation(CenterCrop(),RoundedCorners(radius)))
        .placeholder(R.drawable.ic_broken_image_24)
        .error(R.drawable.ic_broken_image_24)
        .into(this)
}