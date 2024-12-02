package com.example.quicktrip.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityVerifyIdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class VerifyId : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyIdBinding
    private var idImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    idImageUri = uri
                    binding.idImagePlaceholder.setImageURI(uri)
                    binding.uploadIdButton.isEnabled = true
                }
            }
        }

        binding.backBtn5.setOnClickListener {
            finish()
        }
        binding.submitVerificationButton.setOnClickListener {
            idImageUri?.let { uri ->
                uploadIDImageToFirebase(uri)
            }
        }

        binding.uploadIdButton.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun uploadIDImageToFirebase(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("identity_documents/${UUID.randomUUID()}.jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveIDUrlToDatabase(downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload ID image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveIDUrlToDatabase(idImageUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val idData = hashMapOf("idImageUrl" to idImageUrl)

            FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .set(idData)
                .addOnSuccessListener {
                    Toast.makeText(this, "ID uploaded successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save ID data", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
