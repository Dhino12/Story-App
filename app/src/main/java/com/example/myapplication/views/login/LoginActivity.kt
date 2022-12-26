package com.example.myapplication.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.model.LoginUser
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.utils.Results
import com.example.myapplication.views.main.MainActivity
import com.example.myapplication.views.register.RegisterActivity
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private var userModel: User = User()
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = UserPreference(this)

        val factory = ViewModelFactory.getInstance(this, userPreference.getUser().token.toString())
        val viewModel: LoginViewModel by viewModels { factory }

        binding.login.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
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
                    viewModel.login(binding.edEmail.text.toString(), binding.edPassword.text.toString())
                        .observe(this){ result ->
                            if (result != null) {
                                when (result) {
                                    is Results.Loading -> {
                                        binding.progressBar.visibility = View.VISIBLE
                                    }
                                    is Results.Success -> {
                                        binding.progressBar.visibility = View.GONE
                                        Toast.makeText(this, "Login ${result.data.message}", Toast.LENGTH_SHORT).show()
                                        val response = result.data
                                        saveToken(response.loginResult)
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    is Results.Error -> {
                                        binding.progressBar.visibility = View.GONE
                                        Toast.makeText(this, "Error login ${result.data}", Toast.LENGTH_SHORT).show()
                                        Log.e("error Login", result.data)
                                    }
                                }
                            }
                        }
                }
            }
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun saveToken(user:LoginUser) {
        userModel.token = user.token
        userPreference.setUser(userModel)
    }
}