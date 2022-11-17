package com.example.myapplication.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_NAME = "extra_name"
        const val KEY_IMAGE = "extra_image"
        const val KEY_DESC = "extra_desc"
        const val KEY_DATE = "extra_date"
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(KEY_NAME)
        val desc = intent.getStringExtra(KEY_DESC)
        val date = intent.getStringExtra(KEY_DATE)
        val image = intent.getStringExtra(KEY_IMAGE)

        Glide.with(this)
            .load(image)
            .into(binding.ivStory)

        binding.apply {
            tvName.text = name
            tvDesc.text = desc
            tvDate.text = date
        }
    }
}