package com.example.clothing_store.product

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clothing_store.R
import com.example.clothing_store.cart.CartActivity
import com.example.clothing_store.user.Home_user
import com.google.firebase.database.FirebaseDatabase

class DetailActivity2 : AppCompatActivity() {

    private lateinit var detailsize: TextView
    private lateinit var detailname: TextView
    private lateinit var detailprice: TextView
    private lateinit var detailImage: ImageView
    private lateinit var buyNowButton: Button
    private var key: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail2)

        // Initialize views
        detailsize = findViewById(R.id.detailsize)
        detailImage = findViewById(R.id.detailImage)
        detailname = findViewById(R.id.detailName)
        detailprice = findViewById(R.id.detailprice)
        buyNowButton = findViewById(R.id.addtocart)
        val backButton: ImageView = findViewById(R.id.back)

        // Back button functionality
        backButton.setOnClickListener {
            val homeIntent = Intent(this@DetailActivity2, Home_user::class.java)
            homeIntent.putExtra("username", intent.getStringExtra("username"))
            startActivity(homeIntent)
        }

        // Retrieve data from Intent extras
        intent.extras?.let {
            detailsize.text = it.getString("size")
            detailname.text = it.getString("name")
            detailprice.text = it.getInt("price").toString()
            Glide.with(this).load(it.getString("image")).into(detailImage)
            key = it.getString("key")
        }

        // Set OnClickListener for the add to cart button
        buyNowButton.setOnClickListener {
            addToCart()
        }
    }

    // Function to add the item to the cart
    private fun addToCart() {
        if (key == null) {
            Toast.makeText(this, "Product key is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Get a reference to the "cart" node in the Firebase Database
        val username = intent.getStringExtra("username").toString()
        val cartRef = FirebaseDatabase.getInstance().getReference("cart").child(username)

        // Create a HashMap to store the cart item details
        val cartItem = hashMapOf(
            "image" to (intent.getStringExtra("image") ?: ""),
            "size" to detailsize.text.toString(),
            "name" to detailname.text.toString(),
            "key" to key,
            "price" to detailprice.text.toString().toDouble()
        )

        // Add the cart item to the database
        cartRef.child(key.toString()).setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to cart successfully", Toast.LENGTH_SHORT).show()
                // Navigate to the cart activity
                val cartIntent = Intent(this, CartActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                cartIntent.putExtra("username", username)
                startActivity(cartIntent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add item to cart: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
