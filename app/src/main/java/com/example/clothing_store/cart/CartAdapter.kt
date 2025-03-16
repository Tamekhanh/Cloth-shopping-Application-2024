package com.example.clothing_store.cart

import CartItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothing_store.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    private val cartItemList: List<CartItem>, username: String,
    private val onQuantityChange: (CartItem) -> Unit // Callback to handle quantity changes
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    val username = username
    private lateinit var DB: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val sizeTextView: TextView = itemView.findViewById(R.id.item_size)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        private val plusCartBtn: TextView = itemView.findViewById(R.id.plusCartBtn)
        private val minusCartBtn: TextView = itemView.findViewById(R.id.minusCartBtn)
        private val numberItemTxt: TextView= itemView.findViewById(R.id.numberItemTxt)

        fun bind(cartItem: CartItem) {
            nameTextView.text = cartItem.name
            sizeTextView.text = cartItem.size
            priceTextView.text = "$${cartItem.price}"

            Glide.with(imageView.context)
                .load(cartItem.image)
                .into(imageView)

            // Initial quantity setup
            numberItemTxt.text = cartItem.quantity.toString()

            plusCartBtn.setOnClickListener {
                var quantity = numberItemTxt.text.toString().toInt()
                quantity++
                numberItemTxt.text = quantity.toString()
                cartItem.quantity = quantity // Update quantity in cartItem
                onQuantityChange(cartItem) // Notify activity of quantity change
            }

            minusCartBtn.setOnClickListener {
                var quantity = numberItemTxt.text.toString().toInt()
                if (quantity > 1) {
                    quantity--
                    numberItemTxt.text = quantity.toString()
                    cartItem.quantity = quantity // Update quantity in cartItem
                    onQuantityChange(cartItem) // Notify activity of quantity change
                } else {
                    val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("cart").child(username).child(cartItem.key.toString())
                    reference.removeValue()
                }
            }
        }
    }
}
