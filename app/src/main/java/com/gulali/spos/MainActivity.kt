package com.gulali.spos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gulali.spos.database.CustomerDB
import com.gulali.spos.database.CustomerDao
import com.gulali.spos.database.CustomerEntity
import com.gulali.spos.databinding.IndexBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: IndexBinding
    private lateinit var searchHeader: EditText
    private lateinit var customerView: RecyclerView
    private lateinit var serviceView: LinearLayout
    private lateinit var warehouseView: LinearLayout
    private lateinit var memberView: LinearLayout
    private lateinit var bottomMenu: BottomNavigationView
    private lateinit var bottomCenterMenu: FloatingActionButton
    private lateinit var cusDao: CustomerDao
    private lateinit var bottomSheet: BottomSheetDialogFragment

    private var customerAdapter: CustomerAdapter ?= null
    private var linearLayoutManager: LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.index)

        binding = IndexBinding.inflate(layoutInflater)
        searchHeader = findViewById(R.id.search_header)
        customerView = findViewById(R.id.customer_view)
        serviceView = findViewById(R.id.service_view)
        warehouseView = findViewById(R.id.warehouse_view)
        memberView = findViewById(R.id.member_view)
        bottomMenu = findViewById(R.id.bottomNavigationView)
        bottomCenterMenu = findViewById(R.id.fab)
        bottomSheet = BottomSheetFragment()

        customerView.visibility = View.VISIBLE
        serviceView.visibility = View.GONE
        warehouseView.visibility = View.GONE
        memberView.visibility = View.GONE

        // database start
        cusDao = CustomerDB.getCustomerDatabase(applicationContext).customerDAO()
        fetchListCustomer(cusDao.getCustomers())
        // database end

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.customers -> {
                    fetchListCustomer(cusDao.getCustomers())
                    searchHeader.hint = "Search customer"
                    customerView.visibility = View.VISIBLE
                    serviceView.visibility = View.GONE
                    warehouseView.visibility = View.GONE
                    memberView.visibility = View.GONE
                }
                R.id.services -> {
                    searchHeader.hint = "Search service"
                    customerView.visibility = View.GONE
                    serviceView.visibility = View.VISIBLE
                    warehouseView.visibility = View.GONE
                    memberView.visibility = View.GONE
                }
                R.id.warehouse -> {
                    searchHeader.hint = "Search product"
                    customerView.visibility = View.GONE
                    serviceView.visibility = View.GONE
                    warehouseView.visibility = View.VISIBLE
                    memberView.visibility = View.GONE
                }
                R.id.members -> {
                    searchHeader.hint = "Search member"
                    customerView.visibility = View.GONE
                    serviceView.visibility = View.GONE
                    warehouseView.visibility = View.GONE
                    memberView.visibility = View.VISIBLE
                } else -> {
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }

        // Open the BottomSheetFragment when the FAB is clicked
        bottomCenterMenu.setOnClickListener {
            val bottomSheet: BottomSheetDialogFragment = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "BottomSheetFragment")
        }
    }

    override fun onResume(){
        super.onResume()
        fetchListCustomer(cusDao.getCustomers())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchListCustomer(customers: List<CustomerEntity>) {
        customerAdapter = CustomerAdapter(customers, applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        customerView.layoutManager = linearLayoutManager
        customerView.adapter = customerAdapter
        customerAdapter?.notifyDataSetChanged()
    }
}