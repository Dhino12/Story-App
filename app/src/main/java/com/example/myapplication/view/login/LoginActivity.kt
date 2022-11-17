package com.example.myapplication.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private var userModel: User = User()
    private lateinit var userPreference: UserPreference
    private var viewModel: ViewModelFactory? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.
    }
}