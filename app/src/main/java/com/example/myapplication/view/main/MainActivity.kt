package com.example.myapplication.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.view.login.LoginActivity
import com.example.myapplication.view.main.listFragment.ListStoryFragment

class MainActivity : AppCompatActivity() {
    private var token: String = ""
    private lateinit var binding: ActivityMainBinding
    private lateinit var userModel:User
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        userModel = userPreference.getUser()
        token = userModel.token.toString()

        Log.d(this@MainActivity::class.java.simpleName, "TOKEN : ${token}")
        Handler(Looper.getMainLooper()).postDelayed({
            if(token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 300L)

        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(ListStoryFragment::class.java.simpleName)
        if (fragment !is ListStoryFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.HomePage, ListStoryFragment(), ListStoryFragment::class.java.simpleName)
                .commit()
            Log.d(this@MainActivity::class.java.simpleName, "Fragment Name now : " + ListStoryFragment::class.java.simpleName)
        }

        startActivity(Intent(this, LoginActivity::class.java))
    }
}