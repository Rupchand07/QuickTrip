package com.example.quicktrip.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityAddLicenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddLicense : AppCompatActivity() {

    private lateinit var binding: ActivityAddLicenseBinding
    private var imageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    binding.drivingLicenseImage.setImageURI(uri)
                    binding.uploadLicenseBtn.isEnabled = true
                }
            }
        }

        binding.backBtn4.setOnClickListener {
            finish()
        }
        binding.selectImageBtn.setOnClickListener {
            openGallery()
        }

        binding.uploadLicenseBtn.setOnClickListener {
            imageUri?.let { uri ->
                uploadImageToFirebase(uri)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }


    private fun uploadImageToFirebase(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("licenses/${UUID.randomUUID()}.jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveLicenseToDatabase(downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveLicenseToDatabase(imageUrl: String) {
        val licenseData = hashMapOf("licenseUrl" to imageUrl)

        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .set(licenseData)
            .addOnSuccessListener {
                Toast.makeText(this, "License uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload license data", Toast.LENGTH_SHORT).show()
            }
    }
}
