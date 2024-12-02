package com.example.quicktrip.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityEditProfilePictureBinding

class EditProfilePicture : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilePictureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn6.setOnClickListener {
            finish()
        }
        binding.buttonChoosePicture.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.buttonTakePhoto.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    binding.currentProfileImage.setImageURI(imageUri)
                }
                TAKE_PHOTO_REQUEST -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    binding.currentProfileImage.setImageBitmap(photo)
                }
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val TAKE_PHOTO_REQUEST = 2
    }
}