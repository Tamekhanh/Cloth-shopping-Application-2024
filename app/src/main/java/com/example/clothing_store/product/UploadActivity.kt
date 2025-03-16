package com.example.clothing_store.product

import android.annotation.SuppressLint
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import com.example.clothing_store.R
import com.google.firebase.database.ValueEventListener


import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot

class UploadActivity : AppCompatActivity() {
    private lateinit var uploadImage: ImageView
    private lateinit var saveButton: Button
    private lateinit var uploadname: EditText
    private lateinit var uploadsize: Spinner
    private lateinit var uploadprice: EditText
    private lateinit var uploadamount: EditText
    private var imageURL: String? = null
    private var uri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadImage = findViewById(R.id.uploadImage)
        uploadsize = findViewById(R.id.sizeSpinner)
        uploadname = findViewById(R.id.uploadname)
        uploadprice = findViewById(R.id.uploadprice)
        uploadamount = findViewById(R.id.uploadAmount)
        saveButton = findViewById(R.id.saveButton)

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data
                uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UploadActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        uri?.let { uri ->
            val storageReference = FirebaseStorage.getInstance().reference
                .child("Android Images")
                .child(UUID.randomUUID().toString())

            val dialog = AlertDialog.Builder(this@UploadActivity)
                .setCancelable(false)
                .setView(R.layout.progress_layout)
                .create()

            dialog.show()

            storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val urlImage = task.result
                        imageURL = urlImage.toString()
                        // Sau khi tải lên hình ảnh, lấy ID mới từ Firebase và tải dữ liệu
                        getNewIdAndUploadData()
                        dialog.dismiss()
                    }
                }
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this@UploadActivity, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@UploadActivity, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNewIdAndUploadData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("product")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Lấy số lượng sản phẩm hiện có trong Firebase
                val newId = snapshot.childrenCount + 1
                // Tiếp tục tải lên dữ liệu với ID mới
                uploadData(newId.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UploadActivity, "Failed to get new ID", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadData(newId: String) {
        val name = uploadname.text.toString()
        val size = uploadsize.selectedItem.toString()
        val priceStr = uploadprice.text.toString()
        val price: Int? = priceStr.toIntOrNull()
        val amountStr = uploadamount.text.toString()
        val amount: Int? = amountStr.toIntOrNull()

        if (price != null) {
            val dataClass = DataProduct(name,size, price, amount,imageURL, newId)
            FirebaseDatabase.getInstance().getReference("product")
                .child(newId)
                .setValue(dataClass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@UploadActivity, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@UploadActivity, "Save failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this@UploadActivity, "Invalid price", Toast.LENGTH_SHORT).show()
        }
    }
}