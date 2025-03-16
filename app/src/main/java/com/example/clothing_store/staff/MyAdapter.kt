package com.example.clothing_store.staff

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothing_store.R
import com.example.clothing_store.product.DataProduct
import com.example.clothing_store.product.DetailActivity

class MyAdapter(private val context: Context, private var dataList: List<DataProduct>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].dataImage).into(holder.recImage)
        holder.recID.text = dataList[position].key // Sử dụng key thay vì dataID
        holder.recName.text = dataList[position].dataname
        holder.recSize.text = dataList[position].datasize
        holder.recPrice.text = dataList[position].dataprice?.toString() ?: ""
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("ID", dataList[position].key) // Sử dụng key thay vì dataID
                putExtra("Image", dataList[position].dataImage)
                putExtra("Description", dataList[position].datasize)
                putExtra("Title", dataList[position].dataname)
                putExtra("Key", dataList[position].key)
                putExtra("Language", dataList[position].dataprice)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: ArrayList<DataProduct>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recID: TextView = itemView.findViewById(R.id.ID)
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recName: TextView = itemView.findViewById(R.id.nameProduct)
    val recSize: TextView = itemView.findViewById(R.id.recSize)
    val recPrice: TextView = itemView.findViewById(R.id.recLang)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
}
