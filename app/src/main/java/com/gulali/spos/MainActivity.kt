package com.gulali.spos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gulali.spos.adapter.ProductAdapter
import com.gulali.spos.database.CustomerEntity
import com.gulali.spos.database.ProductForViewMenu
import com.gulali.spos.database.SposDB
import com.gulali.spos.database.SposDao
import com.gulali.spos.databinding.IndexBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: IndexBinding
    private lateinit var searchHeader: EditText
    private lateinit var customerView: RecyclerView
    private lateinit var serviceView: RecyclerView
    private lateinit var warehouseView: RecyclerView
    private lateinit var memberView: RecyclerView
    private lateinit var bottomMenu: BottomNavigationView
    private lateinit var bottomCenterMenu: FloatingActionButton
    private lateinit var sposDao: SposDao
    private lateinit var bottomSheet: BottomSheetDialogFragment

    private var customerAdapter: CustomerAdapter? = null
    private var productAdapter: ProductAdapter? = null
    private var linearLayoutManagerCustomer: LinearLayoutManager? = null
    private var linearLayoutManagerProduct: LinearLayoutManager? = null

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

        linearLayoutManagerCustomer = LinearLayoutManager(applicationContext)
        linearLayoutManagerProduct = LinearLayoutManager(applicationContext)

        // database start
        sposDao = SposDB.getSposDatabase(applicationContext).sposDAO()
        fetchListCustomer(sposDao.getCustomers())
        showListProductInMainMenu(sposDao.getProductsForViewMenu())
        // database end

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.customers -> {
                    fetchListCustomer(sposDao.getCustomers())
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
        if (customerView.visibility == View.VISIBLE) {
            fetchListCustomer(sposDao.getCustomers())
            return
        }
        if (warehouseView.visibility == View.VISIBLE) {
            showListProductInMainMenu(sposDao.getProductsForViewMenu())
            return
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchListCustomer(customers: List<CustomerEntity>) {
        customerAdapter = CustomerAdapter(customers, applicationContext)
        customerView.layoutManager = linearLayoutManagerCustomer
        customerView.adapter = customerAdapter
        customerAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showListProductInMainMenu(products: List<ProductForViewMenu>) {
        productAdapter = ProductAdapter(products, applicationContext, contentResolver)
        warehouseView.layoutManager = linearLayoutManagerProduct
        warehouseView.adapter = productAdapter
        productAdapter?.notifyDataSetChanged()
    }
}