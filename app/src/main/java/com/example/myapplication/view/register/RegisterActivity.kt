package com.example.myapplication.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.view.login.LoginActivity
import com.example.myapplication.viewmodel.RegisterViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var viewModel: RegisterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))[RegisterViewModel::class.java]

        binding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.register.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            Log.d(this@RegisterActivity::class.simpleName, "${name} login ${email} - ${password}")

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
                    Log.d(this@RegisterActivity::class.simpleName, "login ${email} - ${password}")
                    viewModel?.register(name, email, password)
                }
            }
        }
    }
}