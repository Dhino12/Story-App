package com.example.myapplication.preference

import android.content.Context

internal class UserPreference (context: Context) {
    companion object {
        private const val PREFS_NAME = "user_preference"
        private const val TOKEN = "token"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val changePref = preference.edit()
        changePref.putString(TOKEN, value.token)
        changePref.apply()
    }

    fun getUser(): User{
        val model = User()
        model.token = preference.getString(TOKEN, "")
        return model
    }
}