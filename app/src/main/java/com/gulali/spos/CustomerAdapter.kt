package com.gulali.spos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gulali.spos.database.CustomerEntity

class CustomerAdapter(listItems: List<CustomerEntity>, private var context: Context) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {
    private var customers: List<CustomerEntity> = listItems

    init {
        this.customers = listItems
    }

    inner class CustomerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var csID: TextView = view.findViewById(R.id.product_name)
        var csName: TextView = view.findViewById(R.id.product_stock)
        var csPhone: TextView = view.findViewById(R.id.product_update)
//        var csCreatedAt: TextView = view.findViewById(R.id.csCreatedAt)
//        var csUpdatedAt: TextView = view.findViewById(R.id.csUpdatedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val cs = customers[position]
        holder.csID.text = cs.id.toString()
        holder.csName.text = cs.name
        holder.csPhone.text = cs.phone
//        holder.csCreatedAt.text = cs.createdAt
//        holder.csUpdatedAt.text = cs.updatedAt
    }
}
