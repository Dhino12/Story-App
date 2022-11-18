package com.example.myapplication.view.main.addStory

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityAddStoryBinding
import com.example.myapplication.view.main.MainActivity
import com.example.myapplication.viewmodel.StoryViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
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
    private var viewModel: StoryViewModel? = null
    private var file: File? = null

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
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[StoryViewModel::class.java]

        binding.btnCamera.setOnClickListener { takePhoto() }

        binding.btnGallery.setOnClickListener { openGallery() }

        binding.btnUpload.setOnClickListener {
            if(token != null && file != null) {
                uploadImage(file!!, binding.edDesc.text.toString())
            } else {
                Toast.makeText(this, "Mohon isikan deskripsi", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel?.let { vm ->
            vm.resultPostUpload.observe(this) {
                if(it) {
                    Toast.makeText(this, "Foto berhasil diupload", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                }
            }

            vm.loading.observe(this) {
                binding.progressBar.visibility = it
            }

            vm.error.observe(this) { error ->
                if(error.isNotEmpty()) {
                    Toast.makeText(this, "ERROR $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImage(image: File, description: String) {
        if(token != null) {
            Log.e(this@AddStoryActivity::class.java.simpleName, "token add : $token")
            viewModel?.postStory("Bearer $token", image, description)
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