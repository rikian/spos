package com.gulali.spos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gulali.spos.R
import com.gulali.spos.database.ProductForViewMenu

class ProductCartAdapter(
    listProducts: MutableList<ProductForViewMenu>,
    private val context: Context
) : RecyclerView.Adapter<ProductCartAdapter.ProductViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null
    private var products: MutableList<ProductForViewMenu> = listProducts

    init {
        this.products = listProducts
    }

    inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var pID: TextView = view.findViewById(R.id.product_id)
        var pName: TextView = view.findViewById(R.id.product_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_in_cart, parent, false)
        val viewHolder = ProductViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(viewHolder.absoluteAdapterPosition)
        }
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
        val product = products[position]
        holder.pID.text = product.productID.toString()
        holder.pName.text = product.productName
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}