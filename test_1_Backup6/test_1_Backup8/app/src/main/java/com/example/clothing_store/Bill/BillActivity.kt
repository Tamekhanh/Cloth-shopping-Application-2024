package com.example.clothing_store.Bill

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
import com.example.clothing_store.cart.Bill
import com.example.clothing_store.cart.CartAdapter
import com.example.clothing_store.user.Home_user
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BillActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var billAdapter: BillAdapter
    private lateinit var databaseReference: DatabaseReference
    private val billItemList = ArrayList<BillItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        val intent = intent
        val username = intent.getStringExtra("username")
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        billAdapter = BillAdapter(billItemList,username.toString())
        recyclerView.adapter = billAdapter


        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            val homeIntent = Intent(this@BillActivity, Home_user::class.java)
            homeIntent.putExtra("username", username)
            startActivity(homeIntent)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("bills").child(username.toString())
        fetchBillItems()

    }

    private fun fetchBillItems() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (itemSnapshot in snapshot.children) {
                    val billItem = itemSnapshot.getValue(BillItem::class.java)
                    billItem?.let {
                        billItemList.add(it)
                    }
                }
                billAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BillActivity, "Failed to load cart items: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}