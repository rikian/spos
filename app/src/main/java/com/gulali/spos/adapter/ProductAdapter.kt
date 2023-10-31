package com.gulali.spos.adapter

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gulali.spos.R
import com.gulali.spos.database.ProductForViewMenu

class ProductAdapter(
    listProducts: List<ProductForViewMenu>,
    private val context: Context,
    private val contentResolver: ContentResolver
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: List<ProductForViewMenu> = listProducts

    init {
        this.products = listProducts
    }

    inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var pID: TextView = view.findViewById(R.id.product_id)
        var pImage: ImageView = view.findViewById(R.id.product_image)
        var pName: TextView = view.findViewById(R.id.product_name)
        var pStock: TextView = view.findViewById(R.id.product_stock)
        var pUnit: TextView = view.findViewById(R.id.product_unit)
        var pUpdate: TextView = view.findViewById(R.id.product_update)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.pID.text = product.productID.toString()
        holder.pName.text = product.productName
        holder.pStock.text = product.productStock.toString()
        holder.pUnit.text = product.productUnit
        holder.pUpdate.text = product.productUpdate
        val file = getFileName(product.productImg)
        if (file != null) {
            Glide.with(context)
                .load(file)
                .into(holder.pImage)
        }
    }

    private fun getFileName(fileName: String): Uri? {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val query = MediaStore.Images.Media.DISPLAY_NAME + " = ?"

        contentResolver.query(collection, projection, query, arrayOf(fileName), null)?.use { cursor ->
            if (cursor.count > 0) {
                cursor.moveToFirst()
                return ContentUris.withAppendedId(
                    collection,
                    cursor.getLong(0)
                )
            } else {
                return null
            }
        }

        return null
    }
}