package com.example.YANK_application.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DrinkMenu(
    val imgDrink: Int,
    val titleDrink: String,
    val priceDrink : Int
) : Parcelable