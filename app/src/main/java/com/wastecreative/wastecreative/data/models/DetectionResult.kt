package com.wastecreative.wastecreative.data.models

import android.graphics.RectF


data class DetectionResult(val boundingBox: RectF, val text: String)
