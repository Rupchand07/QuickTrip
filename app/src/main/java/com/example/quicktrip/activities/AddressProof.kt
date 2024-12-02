package com.example.quicktrip.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityAddressProofBinding

class AddressProof : AppCompatActivity() {

    private lateinit var binding: ActivityAddressProofBinding
    private var selectedFileUri: Uri? = null

    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddressProofBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val proofTypes = arrayOf("Passport", "Driver's License", "Utility Bill", "Voter ID", "Aadhar Card", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, proofTypes)
        binding.proofTypeSpinner.adapter = adapter

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                selectedFileUri = data?.data
                if (selectedFileUri != null) {
                    val fileName = getFileName(selectedFileUri!!)
                    binding.uploadProofButton.text = "Uploaded: $fileName"
                } else {
                    Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.uploadProofButton.setOnClickListener {
            openFilePicker()
        }

        binding.submitButton.setOnClickListener {
            val address = binding.addressField.text.toString()
            val proofType = binding.proofTypeSpinner.selectedItem.toString()

            if (address.isBlank()) {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedFileUri == null) {
                Toast.makeText(this, "Please upload a proof document", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Address: $address\nProof Type: $proofType\nFile: ${getFileName(selectedFileUri!!)}", Toast.LENGTH_LONG).show()

            binding.addressField.text.clear()
            binding.proofTypeSpinner.setSelection(0)
            binding.uploadProofButton.text = "Upload Proof"
            selectedFileUri = null
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        filePickerLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "Unknown"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}
