package com.example.myapplication.views.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.views.login.LoginActivity
import com.example.myapplication.views.main.listFragment.ListStoryFragment
import com.example.myapplication.views.maps.MapsActivity

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

        Log.d(this@MainActivity::class.java.simpleName, token)
        if(token.replace("Bearer ", "").isEmpty()) {
            Log.e(this@MainActivity::class.java.simpleName, "TOKEN : ${token}")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(ListStoryFragment::class.java.simpleName)
        if (fragment !is ListStoryFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.HomePage, ListStoryFragment(), ListStoryFragment::class.java.simpleName)
                .commit()
        }

        supportActionBar?.title = "Story Map"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            R.id.map -> {
                val intent =Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.KEY_TOKEN_MAPS, token)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun logout() {
        userModel.token = ""
        userPreference.setUser(userModel)
        startActivity(Intent(this, LoginActivity::class.java))
        finish();
    }
}