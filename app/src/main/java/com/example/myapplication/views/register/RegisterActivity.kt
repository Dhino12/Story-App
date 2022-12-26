package com.example.myapplication.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.utils.Results
import com.example.myapplication.views.login.LoginActivity
import com.example.myapplication.viewmodel.RegisterViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, "")
        val viewModel: RegisterViewModel by viewModels { factory }

        binding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.register.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            Log.d(this@RegisterActivity::class.simpleName, "$name login $email - $password")

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
                    viewModel.register(email, name, password).observe(this) { results ->
                        when (results) {
                            is Results.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Results.Success -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                            is Results.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "${results.data}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}