package com.example.myapplication.preference

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var token: String? = null
):Parcelable