package com.wastecreative.wastecreative.utils

import android.app.Application
import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wastecreative.wastecreative.R
import java.io.File


fun ImageView.loadImage(url: String?, radius: Int) {
    Glide.with(this.context)
        .load(url)
        .transform(MultiTransformation(CenterCrop(),RoundedCorners(radius)))
        .placeholder(R.drawable.ic_broken_image_24)
        .error(R.drawable.ic_broken_image_24)
        .into(this)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "${System.currentTimeMillis()}.jpg")
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}