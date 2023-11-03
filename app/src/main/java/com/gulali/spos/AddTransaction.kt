package com.gulali.spos

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gulali.spos.adapter.ProductAdapter
import com.gulali.spos.adapter.ProductCartAdapter
import com.gulali.spos.database.ProductForViewMenu
import com.gulali.spos.database.SposDB
import com.gulali.spos.database.SposDao

class AddTransaction: ComponentActivity() {
    private lateinit var searchProduct: AppCompatEditText
    private lateinit var productView: RecyclerView
    private lateinit var cart: RecyclerView
    private lateinit var payout: Button
    private var productAdapter: ProductAdapter? = null
    private var productInCartAdapter: ProductCartAdapter? = null
    private var linearLayoutManagerProduct: LinearLayoutManager? = null
    private var linearLayoutManagerCart: LinearLayoutManager? = null
    private lateinit var sposDao: SposDao

    private var productInCart: MutableList<ProductForViewMenu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_transaction)

        payout = findViewById(R.id.payout)
        searchProduct = findViewById(R.id.idSV)
        sposDao = SposDB.getSposDatabase(applicationContext).sposDAO()
        productView = findViewById(R.id.product_view)
        cart = findViewById(R.id.cart_product)
        linearLayoutManagerProduct = LinearLayoutManager(applicationContext)
        linearLayoutManagerCart = LinearLayoutManager(applicationContext)

        payout.setOnClickListener {
            try {
                showPayment()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        var query = ""
        searchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    if (query == s.toString().lowercase()) {
                        return
                    }
                    query = s.toString().lowercase()
                    val products = sposDao.getProductByName(query)
                    if (products.isEmpty()) {
                        Toast.makeText(applicationContext, "product not found", Toast.LENGTH_SHORT).show()
                        return
                    }
                    showAvailableProducts(products)
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    fun showPayment() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_payment, null)
        val alertDialog = builder.setView(dialogLayout).show()
    }

    fun showQTY(prod: ProductForViewMenu) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_qty, null)

        // text display
        val pdName  = dialogLayout.findViewById<TextView>(R.id.product_name)
        val pdOk = dialogLayout.findViewById<Button>(R.id.btn_qty_ok)

        val alertDialog = builder.setView(dialogLayout).show() // Create and show the AlertDialog

        pdOk.setOnClickListener {
            productInCart.add(prod)
            showProductInCart(productInCart)
            // Close the popup
            alertDialog.dismiss()
        }

        pdName.text = prod.productName
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showProductInCart(products: MutableList<ProductForViewMenu>) {
        productInCartAdapter = ProductCartAdapter(products, applicationContext)
        cart.layoutManager = linearLayoutManagerCart
        cart.adapter = productInCartAdapter
        productInCartAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showAvailableProducts(products: List<ProductForViewMenu>) {
        productAdapter = ProductAdapter(products, applicationContext, contentResolver)
        productAdapter?.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                try {
                    val product = products[position]
                    showQTY(product)
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        productView.layoutManager = linearLayoutManagerProduct
        productView.adapter = productAdapter
        productAdapter?.notifyDataSetChanged()
    }
}

//private val listUnit = mutableListOf<String>()
//
//// Create (Insert) an item to the list
//fun insertUnit(unit: String) {
//    listUnit.add(unit)
//}
//
//// Read (Retrieve) the entire list
//fun readAllUnits(): List<String> {
//    return listUnit
//}
//
//// Read (Retrieve) a specific item by index
//fun readUnitByIndex(index: Int): String? {
//    return if (index >= 0 && index < listUnit.size) {
//        listUnit[index]
//    } else {
//        null
//    }
//}
//
//// Update (Modify) an item by index
//fun updateUnitByIndex(index: Int, newUnit: String): Boolean {
//    return if (index >= 0 && index < listUnit.size) {
//        listUnit[index] = newUnit
//        true
//    } else {
//        false
//    }
//}
//
//// Delete (Remove) an item by index
//fun deleteUnitByIndex(index: Int): Boolean {
//    return if (index >= 0 && index < listUnit.size) {
//        listUnit.removeAt(index)
//        true
//    } else {
//        false
//    }
//}
//
//// Delete (Remove) a specific item
//fun deleteUnit(unit: String): Boolean {
//    return listUnit.remove(unit)
//}