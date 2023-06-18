package com.example.YANK_application.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PackageMenu(
    val imgPackage: Int,
    val titlePackage: String,
    val pricePackage : Int
) : Parcelable
