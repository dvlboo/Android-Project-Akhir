package com.example.YANK_application.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DesertMenu(
    val imgDesert: Int,
    val titleDesert: String,
    val priceDesert : Int
) : Parcelable