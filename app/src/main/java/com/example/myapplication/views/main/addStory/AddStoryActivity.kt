package com.example.myapplication.views.main.addStory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityAddStoryBinding
import com.example.myapplication.utils.Results
import com.example.myapplication.views.main.MainActivity
import com.example.myapplication.viewmodel.StoryViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var photoPath: String
    private var token: String = ""
    private lateinit var factory: ViewModelFactory
    private val viewModel: StoryViewModel? by viewModels {
        factory
    }
    private var file: File? = null
    private lateinit var fusedLocationProvideClient: FusedLocationProviderClient

    companion object {
        val REQUIRED_CAMERA_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!checkPermissionCamera()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_CAMERA_PERMISSION, 10
            )
        }
        token = intent.getStringExtra("extra_token_upload").toString()
        factory = ViewModelFactory.getInstance(this, token)
        fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnCamera.setOnClickListener { takePhoto() }

        binding.btnGallery.setOnClickListener { openGallery() }

        binding.btnUpload.setOnClickListener {
            if(token != null && file != null) {
                uploadImage(file!!, binding.edDesc.text.toString())
            } else {
                Toast.makeText(this, "Mohon isikan deskripsi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(image: File, description: String) {
        if(token != null ) {
            Log.e(this@AddStoryActivity::class.java.simpleName, "token add : $token")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
                return
            }
            val nowLocation = fusedLocationProvideClient?.lastLocation
            Log.e("location", nowLocation.toString())
            nowLocation?.addOnSuccessListener { location: Location? ->
                val lat: Double = location?.latitude ?: 0.0
                val lng: Double = location?.longitude ?: 0.1
                val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart:MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    image.name,
                    requestImageFile
                )
                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())

                Log.e("test", lat.toString())
                Log.e("test", lng.toString())
                Log.e("test", imageMultipart.toString())
                viewModel?.postStory(token, imageMultipart, descriptionRequestBody, lat, lng )
                    ?.observe(this){ result ->
                    if (result == null) return@observe
                    when(result) {
                        is Results.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Results.Success -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Toast.makeText(this@AddStoryActivity, "Success Add Story", Toast.LENGTH_SHORT).show()
                            Log.d("success addStory", result.data.message)

                            val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is Results.Error -> {
                            Toast.makeText(this@AddStoryActivity, "Ooops mohon dicoba lagi", Toast.LENGTH_SHORT).show()
                            Log.e("upload error", result.data)
                        }
                    }
                }

            }

        } else {
            Toast.makeText(this, "Token tidak berhasil dimuat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTmpFile(application).also {
            val photoUri:Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.myapplication",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherCameraIntent.launch(intent)
        }
    }

    private var launcherCameraIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode === RESULT_OK) {
            file = File(photoPath)

            val result = BitmapFactory.decodeFile(file?.path)
            binding.pvImage.setImageBitmap(result)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val choose = Intent.createChooser(intent, "Pilih gambar")
        launcherGalleryIntent.launch(choose)
    }

    private fun checkPermissionCamera() = REQUIRED_CAMERA_PERMISSION.all { permission ->
        ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherGalleryIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result -> if(result.resultCode === RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImage, this)
        file = myFile
        binding.pvImage.setImageURI(selectedImage)
    }
    }

    private fun createTmpFile(context: Context) : File{
        val timeStamp :String = SimpleDateFormat(
            "dd-MMM-yyy", Locale.US
        ).format(System.currentTimeMillis())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }
    private fun uriToFile(selectedImg: Uri, context: Context) : File{
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTmpFile(context)

        val streamInput = contentResolver.openInputStream(selectedImg) as InputStream
        val streamOutput:OutputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var numberOfByte: Int

        while (streamInput.read(buffer).also { numberOfByte = it } > 0) streamOutput.write(buffer, 0, numberOfByte)
        streamOutput.close()
        streamInput.close()

        return myFile
    }
}