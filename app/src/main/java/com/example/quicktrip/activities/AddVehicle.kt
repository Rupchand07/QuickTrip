package com.example.quicktrip.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityAddVehicleBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddVehicle : AppCompatActivity() {
    private lateinit var binding: ActivityAddVehicleBinding
    private lateinit var firestore: FirebaseFirestore

    private val vehicleTypes = arrayOf("Type","Car", "Bike", "Truck", "Van")
    private val vehicleModels = mapOf(
        "Car" to arrayOf("Model","Honda Civic", "Toyota Corolla", "Ford F-150", "Chevrolet Silverado"),
        "Bike" to arrayOf("Model","Yamaha YZF-R3", "Kawasaki Ninja", "Honda CB500F", "Ducati Monster"),
        "Truck" to arrayOf("Model","Ford F-250", "Chevrolet Silverado 2500", "RAM 2500", "Toyota Tundra"),
        "Van" to arrayOf("Model","Honda Odyssey", "Toyota Sienna", "Ford Transit", "Mercedes-Benz Sprinter")
    )
    private val vehicleColors = arrayOf("Color","Red", "Blue", "Green", "Black", "White")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        setupSpinners()

        binding.addVehicleButton.setOnClickListener {
            addVehicle()
        }
        binding.backBtn8.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehicleTypeSpinner.adapter = typeAdapter

        binding.vehicleTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = vehicleTypes[position]
                updateModelSpinner(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val colorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleColors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehicleColorSpinner.adapter = colorAdapter

        updateModelSpinner(vehicleTypes[0])
    }

    private fun updateModelSpinner(vehicleType: String) {
        val models = vehicleModels[vehicleType] ?: emptyArray()
        val modelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, models)
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehicleModelSpinner.adapter = modelAdapter
    }

    private fun addVehicle() {
        val vehicleType = binding.vehicleTypeSpinner.selectedItem.toString()
        val vehicleModel = binding.vehicleModelSpinner.selectedItem.toString()
        val vehicleColor = binding.vehicleColorSpinner.selectedItem.toString()

        if (vehicleType.isNotEmpty() && vehicleModel.isNotEmpty() && vehicleColor.isNotEmpty()) {
            val vehicleData = hashMapOf(
                "type" to vehicleType,
                "model" to vehicleModel,
                "color" to vehicleColor
            )

            firestore.collection("vehicles").add(vehicleData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Vehicle added successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding vehicle: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        binding.vehicleTypeSpinner.setSelection(0)
        updateModelSpinner(vehicleTypes[0])
        binding.vehicleColorSpinner.setSelection(0)
    }
}
