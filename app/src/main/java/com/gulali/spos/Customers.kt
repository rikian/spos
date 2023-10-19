package com.gulali.spos

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gulali.spos.database.CustomerDB
import com.gulali.spos.database.CustomerEntity

class Customers() : Fragment() {
    private lateinit var recycleTask: RecyclerView
    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
//    private val db = CustomerDB.getCustomerDatabase(requireContext())
//    private val cusDao = db.customerDAO()
//    private val customers: List<CustomerEntity> = cusDao.getCustomers()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_customers, container, false)
//        val bookslist = arguments.getParcelableArrayList("")
//        recycleTask = view.findViewById(R.id.listCustomer)
//        customerAdapter = CustomerAdapter(this.customers, requireContext())
//        linearLayoutManager = LinearLayoutManager(requireContext())
//        recycleTask.layoutManager = linearLayoutManager
//        recycleTask.adapter = customerAdapter
//        customerAdapter?.notifyDataSetChanged()
        return view
    }
}