package com.example.clothing_store.product

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clothing_store.R
import com.github.clans.fab.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var detailsize: TextView
    private lateinit var detaid: TextView
    private lateinit var detailname: TextView
    private lateinit var detailprice: TextView
    private lateinit var detailImage: ImageView
    private lateinit var editButton: FloatingActionButton
    private var key: String = ""
    private var imageUrl: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detaid = findViewById(R.id.detailID)
        detailsize = findViewById(R.id.detailsize)
        detailImage = findViewById(R.id.detailImage)
        detailname = findViewById(R.id.detailName)
        editButton = findViewById(R.id.editButton)
        detailprice = findViewById(R.id.detailprice)

        val bundle = intent.extras
        bundle?.let {
            detaid.text = it.getString("id")
            detailsize.text = it.getString("Description")
            detailname.text = it.getString("Title")
            detailprice.text = "$"+it.getInt("Language").toString()+".00"
            key = it.getString("Key", "")
            imageUrl = it.getString("Image", "")
            Glide.with(this).load(it.getString("Image")).into(detailImage)
        }

        editButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
                .putExtra("id", detaid.text.toString())
                .putExtra("name", detailname.text.toString())
                .putExtra("size", detailsize.text.toString())
                .putExtra("price", detailprice.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        }
    }
}
