package com.example.clothing_store.product

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clothing_store.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class UpdateActivity : AppCompatActivity() {
    private lateinit var updateImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var updateTitle: EditText
    private lateinit var sizeSpinner: Spinner
    private lateinit var updateLang: EditText
    private lateinit var updateamount: EditText
    private var imageUrl: String = ""
    private lateinit var key: String
    private var oldImageUrl: String = ""
    private var uri: Uri? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        updateButton = findViewById(R.id.updateButton)
        updateImage = findViewById(R.id.updateImage)
        updateTitle = findViewById(R.id.updateTitle)
        updateamount = findViewById(R.id.updateAmount)
        sizeSpinner = findViewById(R.id.sizeSpinner)
        updateLang = findViewById(R.id.updateLang)

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data
                updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        updateButton.setOnClickListener {
            saveData()
        }

        val bundle: Bundle? = intent.extras
        bundle?.let {
            key = bundle.getString("Key") ?: ""
            Glide.with(this@UpdateActivity).load(bundle.getString("Image")).into(updateImage)
            updateTitle.setText(bundle.getString("Title"))
            sizeSpinner.setSelection(getIndexForDescription(bundle.getString("Description")))
            updateLang.setText(bundle.getInt("Language").toString())
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("product").child(key)
    }

    private fun saveData() {
        uri?.let { selectedUri ->
            storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Images")
                .child(selectedUri.lastPathSegment ?: "")

            val dialog = AlertDialog.Builder(this@UpdateActivity)
                .setCancelable(false)
                .setView(R.layout.progress_layout)
                .create()

            dialog.show()

            storageReference.putFile(selectedUri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val urlImage = task.result
                        imageUrl = urlImage.toString()
                        updateProduct()
                        dialog.dismiss()
                    }
                }
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UpdateActivity, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UpdateActivity, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProduct() {
        val name = updateTitle.text.toString()
        val size = sizeSpinner.selectedItem.toString()
        val priceStr = updateLang.text.toString()
        val price: Int? = priceStr.toIntOrNull()
        val amountStr = updateLang.text.toString()
        val amount: Int? = amountStr.toIntOrNull()

        if (price != null) {
            val dataClass = DataProduct(name, size, price, amount, imageUrl, key)

            databaseReference.setValue(dataClass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (oldImageUrl != imageUrl) {
                            deleteOldImageFromStorage()
                        }
                        Toast.makeText(this@UpdateActivity, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@UpdateActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this@UpdateActivity, "Invalid language", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteOldImageFromStorage() {
        if (oldImageUrl.isNotEmpty()) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl)
            storageReference.delete()
                .addOnSuccessListener {
                    // Deleted successfully
                }
                .addOnFailureListener {
                    // Failed to delete
                }
        }
    }

    private fun getIndexForDescription(description: String?): Int {
        val adapter = sizeSpinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == description) {
                return i
            }
        }
        return 0
    }
}
