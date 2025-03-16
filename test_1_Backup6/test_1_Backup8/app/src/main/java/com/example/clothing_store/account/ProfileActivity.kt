package com.example.clothing_store.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.clothing_store.R
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var profilefirstName: TextView
    private lateinit var profilelastName: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        profilefirstName = findViewById(R.id.profilefirstName)
        profilelastName = findViewById(R.id.profilelastName)
        profilePhone = findViewById(R.id.profilePhone)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        titleName = findViewById(R.id.titlehello)
        titleUsername = findViewById(R.id.titleUsername)
        editProfile = findViewById(R.id.editButton)
        val backButton: ImageView = findViewById(R.id.back)

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }

        showUserData()

        editProfile.setOnClickListener {
            editUserData()
        }
    }

    private fun showUserData() {
        val intent = intent
        val firstnameUser = intent.getStringExtra("nameFirst")
        val lastnameUser = intent.getStringExtra("nameLast")
        val phone = intent.getStringExtra("phone")
        val emailUser = intent.getStringExtra("email")
        val usernameUser = intent.getStringExtra("username")

        titleName.text = "Hello!"
        titleUsername.text = usernameUser
        profilefirstName.text = firstnameUser
        profilelastName.text = lastnameUser
        profilePhone.text = phone
        profileEmail.text = emailUser
        profileUsername.text = usernameUser
    }

    private fun editUserData() {
        val username = profileUsername.text.toString().trim()
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val query: Query = reference.orderByChild("username").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nameFromDB = snapshot.child(username).child("name").getValue(String::class.java)
                    val emailFromDB = snapshot.child(username).child("email").getValue(String::class.java)

                    val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("name", nameFromDB)
                    intent.putExtra("email", emailFromDB)
                    intent.putExtra("username", username)

                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
