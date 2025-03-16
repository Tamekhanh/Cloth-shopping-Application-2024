// SignUpActivity.kt
package com.example.clothing_store.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.example.clothing_store.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var signupfirstName: EditText
    private lateinit var signuplastName: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupPhone: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signupfirstName = findViewById(R.id.signup_firstname)
        signuplastName = findViewById(R.id.signup_lastname)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        signupPhone = findViewById(R.id.signup_phone)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val firstname = signupfirstName.text.toString()
            val lastname = signuplastName.text.toString()
            val email = signupEmail.text.toString()
            val username = signupUsername.text.toString()
            val password = signupPassword.text.toString()
            val phone = signupPhone.text.toString()

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.getReference("user")
            if (validatelastname()&&validatefirstname()&&validateusername()&&validateusername()&&
                validatepassword()&&validateEmail()&&validatephone()) {
                reference.child(username)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Username already exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val userdata =
                                    UserData(firstname, lastname, email, username, password, phone)
                                reference.child(username).setValue(userdata)
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "You have signed up successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
        }


        loginRedirectText.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validatefirstname(): Boolean {
        val password = signupfirstName.text.toString().trim()
        return if (password.isEmpty()) {
            signupfirstName.error = "Enter Information"
            false
        } else {
            signupfirstName.error = null
            true
        }
    }

    private fun validatelastname(): Boolean {
        val password = signuplastName.text.toString().trim()
        return if (password.isEmpty()) {
            signuplastName.error = "Enter Information"
            false
        } else {
            signuplastName.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val password = signupEmail.text.toString().trim()
        return if (password.isEmpty()) {
            signupEmail.error = "Enter Information"
            false
        } else {
            signupEmail.error = null
            true
        }
    }

    private fun validateusername(): Boolean {
        val password = signupUsername.text.toString().trim()
        return if (password.isEmpty()) {
            signupUsername.error = "Enter Information"
            false
        } else {
            signupUsername.error = null
            true
        }
    }

    private fun validatepassword(): Boolean {
        val password = signupPassword.text.toString().trim()
        return if (password.isEmpty()) {
            signupPassword.error = "Enter Information"
            false
        } else {
            signupPassword.error = null
            true
        }
    }

    private fun validatephone(): Boolean {
        val password = signupPhone.text.toString().trim()
        return if (password.isEmpty()) {
            signupPhone.error = "Enter Information"
            false
        } else {
            signupPhone.error = null
            true
        }
    }
}
