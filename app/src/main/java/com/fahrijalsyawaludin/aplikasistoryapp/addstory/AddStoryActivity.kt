package com.fahrijalsyawaludin.aplikasistoryapp.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fahrijalsyawaludin.aplikasistoryapp.R
import com.fahrijalsyawaludin.aplikasistoryapp.databinding.ActivityAddstoryBinding
import com.fahrijalsyawaludin.aplikasistoryapp.getImageUri
import com.fahrijalsyawaludin.aplikasistoryapp.main.MainActivity
import com.fahrijalsyawaludin.aplikasistoryapp.reduceFileImage
import com.fahrijalsyawaludin.aplikasistoryapp.uriToFile
import com.fahrijalsyawaludin.aplikasistoryapp.view.ViewModelFactory


class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddstoryBinding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddstoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.infoLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.infoResponse.observe(this) { message ->
            showToast(message.message!!)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        viewModel.infoResponse.observe(this) {
            if (!it.error!!) {
                AlertDialog.Builder(this).apply {
                    setTitle("Let's Go!!")
                    setMessage("Makasih udah Upload")
                    setPositiveButton("Gass") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                if (it.message == getString(R.string.empty_image_warning)) {
                    if (binding.descriptionEditText.text!!.isEmpty()) {
                        binding.descriptionEditText.error = "Harus isi deskripsi"
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle("Waduhh!")
                        setMessage(it.message)
                        setPositiveButton("OK") { _, _ ->
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.descriptionEditText.text.toString()
            viewModel.add(imageFile, description)

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}