package com.example.clothing_store.Bill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothing_store.R

class BillAdapter(
    private val billItemList: List<BillItem>, username: String,
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {
    val username = username
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_bill_adapter, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val BillItem = billItemList[position]
        holder.bind(BillItem)
    }

    override fun getItemCount(): Int {
        return billItemList.size
    }

    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val IDTextView: TextView = itemView.findViewById(R.id.bill_id)
        private val addressTextView: TextView = itemView.findViewById(R.id.bill_address)
        private val priceTextView: TextView = itemView.findViewById(R.id.bill_totalprice)
        private val CreateDateTextView: TextView = itemView.findViewById(R.id.bill_Create_Date)

        fun bind(billitem: BillItem) {
            IDTextView.text = "#"+billitem.key.toString() + username
            addressTextView.text = billitem.address.toString()
            priceTextView.text = "${billitem.total.toString()}"
            CreateDateTextView.text = billitem.createDate.toString()
        }
    }
}

