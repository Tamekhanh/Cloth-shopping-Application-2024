package com.example.clothing_store.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.clothing_store.R


class EditProfileActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button
    private lateinit var reference: DatabaseReference

    private var nameUser: String? = null
    private var emailUser: String? = null
    private var usernameUser: String? = null
    private var passwordUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        
        reference = FirebaseDatabase.getInstance().getReference("users")

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)

        showData()

        saveButton.setOnClickListener {
            if (isNameChanged() || isPasswordChanged() || isEmailChanged()) {
                Toast.makeText(this@EditProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@EditProfileActivity, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNameChanged(): Boolean {
        val newName = editName.text.toString()
        if (nameUser != newName) {
            reference.child(usernameUser!!).child("name").setValue(newName)
            nameUser = newName
            return true
        }
        return false
    }

    private fun isEmailChanged(): Boolean {
        val newEmail = editEmail.text.toString()
        if (emailUser != newEmail) {
            reference.child(usernameUser!!).child("email").setValue(newEmail)
            emailUser = newEmail
            return true
        }
        return false
    }

    private fun isPasswordChanged(): Boolean {
        val newPassword = editPassword.text.toString()
        if (passwordUser != newPassword) {
            reference.child(usernameUser!!).child("password").setValue(newPassword)
            passwordUser = newPassword
            return true
        }
        return false
    }

    private fun showData() {
        val intent = intent
        nameUser = intent.getStringExtra("name")
        emailUser = intent.getStringExtra("email")
        usernameUser = intent.getStringExtra("username")
        passwordUser = intent.getStringExtra("password")

        editName.setText(nameUser)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPassword.setText(passwordUser)
    }
}
