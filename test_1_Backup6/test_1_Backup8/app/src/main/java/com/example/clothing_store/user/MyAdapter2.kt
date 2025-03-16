// MyAdapter2.kt
package com.example.clothing_store.product

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

class MyAdapter2(private val context: Context, private var dataList: List<DataProduct>) :
    RecyclerView.Adapter<MyViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder2(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        Glide.with(context).load(dataList[position].dataImage).into(holder.recImage)
        holder.recID.text = " "
        holder.recIDTitle.text = " "
        holder.recName.text = dataList[position].dataname
        holder.recSize.text = dataList[position].datasize
        holder.recPrice.text = dataList[position].dataprice?.toString() ?: ""
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity2::class.java).apply {
                putExtra("key", dataList[position].key)
                putExtra("name", dataList[position].dataname)
                putExtra("size", dataList[position].datasize)
                putExtra("price", dataList[position].dataprice ?: 0)
                putExtra("image", dataList[position].dataImage)
                putExtra("username", dataList[position].username)
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

class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recID: TextView = itemView.findViewById(R.id.ID)
    val recIDTitle: TextView = itemView.findViewById(R.id.IDtitle)
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recName: TextView = itemView.findViewById(R.id.nameProduct)
    val recSize: TextView = itemView.findViewById(R.id.recSize)
    val recPrice: TextView = itemView.findViewById(R.id.recLang)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
}
