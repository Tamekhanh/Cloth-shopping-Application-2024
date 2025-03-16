package com.example.clothing_store.staff

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothing_store.R
import com.example.clothing_store.product.DataProduct
import com.example.clothing_store.product.UploadActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.Locale

class Home_staff : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var searchView: SearchView
    private val dataList = ArrayList<DataProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        searchView = findViewById(R.id.search)
        searchView.clearFocus()

        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        adapter = MyAdapter(this, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("product")
        dialog.show()

        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataProduct::class.java)
                    dataClass?.let {
                        it.key = itemSnapshot.key
                        dataList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<DataProduct>()
        for (dataClass in dataList) {
            if (dataClass.dataname?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::eventListener.isInitialized) {
            databaseReference.removeEventListener(eventListener)
        }
    }
}
