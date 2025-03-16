// LoginActivity.kt
package com.example.clothing_store.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.example.clothing_store.R
import com.example.clothing_store.staff.Home_staff
import com.example.clothing_store.user.Home_user

class LoginActivity : AppCompatActivity() {
    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signupRedirectText)

        loginButton.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser()
                checkstaff()
            }
        }

        signupRedirectText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateUsername(): Boolean {
        val username = loginUsername.text.toString().trim()
        return if (username.isEmpty()) {
            loginUsername.error = "Username cannot be empty"
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = loginPassword.text.toString().trim()
        return if (password.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("user")
        val checkUserQuery: Query = reference.orderByChild("username").equalTo(userUsername)

        checkUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)

                        if (passwordFromDB == userPassword) {
                            val firstnameFromDB = userSnapshot.child("nameFirst").getValue(String::class.java)
                            val lastnameFromDB = userSnapshot.child("nameLast").getValue(String::class.java)
                            val PhoneFromDB = userSnapshot.child("phone").getValue(String::class.java)
                            val emailFromDB = userSnapshot.child("email").getValue(String::class.java)
                            val intent = Intent(this@LoginActivity, Home_user()::class.java)
                            intent.putExtra("nameFirst", firstnameFromDB)
                            intent.putExtra("nameLast", lastnameFromDB)
                            intent.putExtra("phone", PhoneFromDB)
                            intent.putExtra("email", emailFromDB)
                            intent.putExtra("username", userUsername)
                            intent.putExtra("password", passwordFromDB)
                            startActivity(intent)
                            return
                        } else {
                            loginUsername.error = "Username or Password wrong"
                            loginUsername.requestFocus()
                        }
                    }
                } else {
                    loginUsername.error = "Username or Password wrong"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun checkstaff() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("staff")
        val checkUserQuery: Query = reference.orderByChild("username").equalTo(userUsername)

        checkUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)

                        if (passwordFromDB == userPassword) {
                            val nameFromDB = userSnapshot.child("name").getValue(String::class.java)
                            val emailFromDB = userSnapshot.child("email").getValue(String::class.java)
                            val intent = Intent(this@LoginActivity, Home_staff::class.java)
                            intent.putExtra("name", nameFromDB)
                            intent.putExtra("email", emailFromDB)
                            intent.putExtra("username", userUsername)
                            intent.putExtra("password", passwordFromDB)
                            startActivity(intent)
                            return
                        } else {
                            loginUsername.error = "Username or Password wrong"
                            loginUsername.requestFocus()
                        }
                    }
                } else {
                    loginUsername.error = "Username or Password wrong"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
