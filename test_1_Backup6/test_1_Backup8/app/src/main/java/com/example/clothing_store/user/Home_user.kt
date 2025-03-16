package com.example.clothing_store.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothing_store.Bill.BillActivity
import com.example.clothing_store.R
import com.example.clothing_store.account.ProfileActivity
import com.example.clothing_store.cart.CartActivity
import com.example.clothing_store.product.DataProduct
import com.example.clothing_store.product.MyAdapter2
import com.google.firebase.database.*
import com.google.firebase.database.ktx.values
import java.util.Locale

class Home_user : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter2: MyAdapter2
    private lateinit var cartButton: Button
    private lateinit var billButton: Button
    private lateinit var profileButton: Button
    private lateinit var searchView: SearchView
    private val dataList = ArrayList<DataProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        profileButton = findViewById(R.id.button_profile)
        cartButton = findViewById(R.id.button_cart)
        billButton = findViewById(R.id.button_bill)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)
        searchView.clearFocus()
        val intent = intent
        val username = intent.getStringExtra("username")
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        val firstname = intent.getStringExtra("nameFirst")
        val lastname = intent.getStringExtra("nameLast")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        adapter2 = MyAdapter2(this, dataList)
        recyclerView.adapter = adapter2

        databaseReference = FirebaseDatabase.getInstance().getReference("product")

        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataProduct::class.java)
                    dataClass?.let {
                        it.key = itemSnapshot.key
                        it.username = username.toString()
                        dataList.add(it)
                    }
                }
                adapter2.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        cartButton.setOnClickListener {
            val intent = Intent(this@Home_user, CartActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        billButton.setOnClickListener {
            val intent = Intent(this@Home_user, BillActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

        profileButton.setOnClickListener {
            val intent = Intent(this@Home_user, ProfileActivity::class.java)
            intent.putExtra("nameFirst", firstname.toString())
            intent.putExtra("nameLast", lastname.toString())
            intent.putExtra("phone", phone.toString())
            intent.putExtra("email", email.toString())
            intent.putExtra("username", username)
            startActivity(intent)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<DataProduct>()
        for (dataClass in dataList) {
            if (dataClass.dataname?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true) {
                searchList.add(dataClass)
            }
        }
        adapter2.searchDataList(searchList)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::eventListener.isInitialized) {
            databaseReference.removeEventListener(eventListener)
        }
    }
}
