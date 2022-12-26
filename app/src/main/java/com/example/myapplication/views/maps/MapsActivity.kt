package com.example.myapplication.views.maps

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMapsBinding
import com.example.myapplication.model.Story
import com.example.myapplication.utils.Results
import com.example.myapplication.views.detail.DetailActivity
import com.example.myapplication.viewmodel.StoryViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var token: String
    private lateinit var factory: ViewModelFactory
    private val storyMapsViewModel: StoryViewModel by viewModels { factory }

    companion object {
        var KEY_TOKEN_MAPS = "extra_token_maps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(KEY_TOKEN_MAPS).toString()
        Log.e("token", token)
        val authToken = "Bearer $token"

        factory = ViewModelFactory.getInstance(this, authToken)
        storyMapsViewModel.loadStoryLocation(token)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (token == null)
                return Toast.makeText(this, "oops sepertinya anda belum login", Toast.LENGTH_SHORT).show()
            storyMapsViewModel.loadStoryLocation(token).observe(this) { result ->
                if (result == null) return@observe
                when(result) {
                    is Results.Loading -> Toast.makeText(this, "tunggu sebentar", Toast.LENGTH_SHORT).show()
                    is Results.Success ->{
                        for (story in result.data.listStory) {
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        story.lat?.toDouble() ?: 0.0,
                                        story.lon?.toDouble() ?: 0.0
                                    )
                                )
                            )?.tag = story
                        }
                    }
                    is Results.Error -> Toast.makeText(this, "kesalahan saat memuat map", Toast.LENGTH_SHORT).show()
                }
            }

            mMap.setOnInfoWindowClickListener { marker ->
                val data: Story = marker.tag as Story
                routeToDetailStory(data)
            }

            mMap.uiSettings.isZoomControlsEnabled = true
            getMyLocation()
            setMapStyle()
    }

    private fun routeToDetailStory(data: Story) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.KEY_NAME, data.name)
        intent.putExtra(DetailActivity.KEY_IMAGE, data.photoUrl)
        intent.putExtra(DetailActivity.KEY_DESC, data.description)
        intent.putExtra(DetailActivity.KEY_DATE, data.createdAt)
        intent.putExtra(DetailActivity.KEY_LAT, data.lat)
        intent.putExtra(DetailActivity.KEY_LNG, data.lon)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(intent)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("map_style", "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("resource notfound", e.message.toString())
        }
    }
}