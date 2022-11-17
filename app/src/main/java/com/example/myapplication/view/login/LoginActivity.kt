package com.example.myapplication.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private var userModel: User = User()
    private lateinit var userPreference: UserPreference
    private var viewModel: LoginViewModel? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this@LoginActivity))[LoginViewModel::class.java]


        binding.login.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            Log.d(this@LoginActivity::class.java.simpleName, Patterns.EMAIL_ADDRESS.matcher(email).matches().toString())
            Log.d(this@LoginActivity::class.simpleName, "login ${email} - ${password}")

            when {
                email.isEmpty() or password.isEmpty() -> {
                    Toast.makeText(this, "Masukan email dan pasword dengan benar", Toast.LENGTH_SHORT).show()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Penulisan email kurang tepat gunakan contoh ha@email.com", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password minimal 7", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.d(this@LoginActivity::class.simpleName, "login ${email} - ${password}")
                    viewModel?.login(email, password)
                }
            }
        }

        binding.register.setOnClickListener {

        }
        viewModel?.let { vmLogin ->
            vmLogin.loading.observe(this) {
                binding.progressBar.visibility = it
            }

            vmLogin.error.observe(this) { error ->
                if (error.isNotEmpty()) {
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}