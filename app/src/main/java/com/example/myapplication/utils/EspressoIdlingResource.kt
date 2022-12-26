package com.example.myapplication.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "GLOBALS"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun incremental() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if(!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingResource.incremental()
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement()
    }
}