package com.example.clothing_store.cart

import CartItem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothing_store.R
import com.example.clothing_store.Bill.BillActivity
import com.example.clothing_store.user.Home_user
import com.google.firebase.database.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseReference: DatabaseReference
    private val cartItemList = ArrayList<CartItem>()

    private lateinit var totalFeeTextView: TextView
    private lateinit var deliveryTextView: TextView
    private lateinit var taxTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var addressText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart2)

        val intent = intent
        val username = intent.getStringExtra("username")
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItemList, username.toString()) { cartItem ->
            calculateTotals() // Recalculate totals when item quantity changes
        }
        recyclerView.adapter = cartAdapter

        totalFeeTextView = findViewById(R.id.totalFee)
        deliveryTextView = findViewById(R.id.delivery)
        taxTextView = findViewById(R.id.tax)
        totalTextView = findViewById(R.id.total)
        checkoutButton = findViewById(R.id.checkoutButton)
        addressText = findViewById(R.id.addressText)

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            val homeIntent = Intent(this@CartActivity, Home_user::class.java)
            homeIntent.putExtra("username", username)
            startActivity(homeIntent)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(username.toString())
        fetchCartItems()

        checkoutButton.setOnClickListener {
            if (validateAddress() && databaseReference !=null ) {
                saveBillToDatabase(username.toString())
                val homeIntent = Intent(this@CartActivity, BillActivity::class.java)
                homeIntent.putExtra("username", username)
                startActivity(homeIntent)
            } else {
                addressText.error= "Address must not empty"
            }

        }
    }

    private fun fetchCartItems() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(CartItem::class.java)
                    cartItem?.let {
                        cartItemList.add(it)
                    }
                }
                cartAdapter.notifyDataSetChanged()
                calculateTotals() // Calculate totals after fetching items
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Failed to load cart items: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun calculateTotals() {
        val percentTax = 0.1 // 10% tax rate
        val deliveryFee: Double = 0.5
        val itemTotal = cartItemList.sumByDouble { it.quantity * (it.price ?: 0.0) }
        val tax = Math.round((itemTotal * percentTax) * 100) / 100.0
        val total = Math.round((itemTotal + tax + deliveryFee) * 100) / 100.0
        totalFeeTextView.text = "$$itemTotal"
        deliveryTextView.text = "$$deliveryFee"
        taxTextView.text = "$$tax"
        totalTextView.text = "$$total"
    }

    private fun saveBillToDatabase(username: String) {
        val billsReference = FirebaseDatabase.getInstance().getReference("bills").child(username)
        billsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val billId = snapshot.childrenCount + 1

                val newBillRef = billsReference.child(billId.toString())

                val bill = Bill(
                    items = cartItemList,
                    subtotal = totalFeeTextView.text.toString(),
                    deliveryFee = deliveryTextView.text.toString(),
                    address = addressText.text.toString(),
                    tax = taxTextView.text.toString(),
                    total = totalTextView.text.toString(),
                    key = Integer.parseInt(billId.toString())
                )

                newBillRef.setValue(bill).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@CartActivity, "Bill saved successfully!", Toast.LENGTH_SHORT).show()
                        cartItemList.clear()
                        cartAdapter.notifyDataSetChanged()
                        calculateTotals()
                        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(username)
                        databaseReference.removeValue()

                        // Start BillActivity and pass the bill details

                    } else {
                        Toast.makeText(this@CartActivity, "Failed to save bill: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Failed to retrieve bill count: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun validateAddress(): Boolean {
        val address = addressText.text.toString().trim()
        return if (address.isEmpty()) {
            addressText.error = "Address cannot be empty"
            false
        } else {
            addressText.error = null
            true
        }
    }
}

data class Bill(
    val items: List<CartItem>,
    val subtotal: String,
    val deliveryFee: String,
    val address: String,
    val tax: String,
    val total: String,
    val key: Int
) : Serializable
