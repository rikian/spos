package com.gulali.spos

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.VolumeShaper.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.produceState
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gulali.spos.database.ProductEntity
import com.gulali.spos.helper.NoEnterInputFilter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class AddProduct: ComponentActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var nPName: EditText
    private lateinit var testImage: ImageView
    private lateinit var imagePath: String
    private lateinit var spinner: Spinner
    private lateinit var btnMinQty: Button
    private lateinit var btnPlusQty: Button
    private lateinit var inpQty: EditText
    private lateinit var btnTakeImgProduct: ImageButton
    private lateinit var imagePrevProduct: ImageView
    private lateinit var ancPurchase: EditText
    private lateinit var ancPrice: EditText
    private lateinit var btnSaveProduct: Button
    private lateinit var productEntity: ProductEntity

    // Variable to hold the captured image
    private var capturedImage: Bitmap? = null
    private var unit = arrayOf<String?>("kg", "pcs", "box", "create new")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

        // initial permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }

        // initial layout
        btnBack = findViewById(R.id.btn_back_tbc)
        btnTakeImgProduct = findViewById(R.id.btn_take_img_product)
        imagePrevProduct = findViewById(R.id.img_prev_product)
        nPName = findViewById(R.id.anp_edt_name)
        nPName.filters = arrayOf(NoEnterInputFilter())
        btnMinQty = findViewById(R.id.anp_qty_min)
        btnPlusQty = findViewById(R.id.anp_qty_plus)
        inpQty = findViewById(R.id.anp_qty_tot)
        ancPurchase = findViewById(R.id.anp_edt_purchase)
        ancPrice = findViewById(R.id.anp_priceA)
        btnSaveProduct = findViewById(R.id.btn_save_product)

        // initiate product entity
        productEntity = ProductEntity(
            image = "default.jpeg",
            name = "",
            quantity = 0,
            unit = 0,
            purchase = 0,
            price = 0,
            createdAt = "",
            updatedAt = ""
        )

        spinner = findViewById(R.id.spinner)

        // take image section
        btnTakeImgProduct.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }

        // quantity section
        btnMinQty.setOnClickListener {
            val value = inpQty.text.toString().toIntOrNull()
            if (value != null && value > 0) {
                inpQty.setText((value - 1).toString())
            }
        }

        btnPlusQty.setOnClickListener {
            val value = inpQty.text.toString().toIntOrNull()
            if (value != null) {
                inpQty.setText((value + 1).toString())
            }
        }

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, unit)
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = ad

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedUnit = unit[position]

                if (selectedUnit == "create new") {
                    Toast.makeText(applicationContext, "you chose create new", Toast.LENGTH_SHORT).show()
                    // Open a new activity to create a new unit
                    val intent = Intent(applicationContext, AddUnit::class.java)
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected (if needed)
            }
        }

        // purchase section
        var cursorPositionPurchase = 0
        ancPurchase.setRawInputType(2)
        ancPurchase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cursorPositionPurchase = ancPurchase.selectionStart
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val actualPurchase = rupiahToInt(s.toString())
                    if (productEntity.purchase == actualPurchase) {
                        return
                    }
                    productEntity.purchase = actualPurchase
                    val parseToRupiah = parseIntToRupiah(productEntity.purchase)
                    ancPurchase.setText(parseToRupiah)

                    // Set the cursor position to the stored position
                    ancPurchase.setSelection(
                        if (cursorPositionPurchase <= parseToRupiah.length) cursorPositionPurchase else parseToRupiah.length
                    )
                    return
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "value must be number", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // price section
        var cursorPositionPrice = 0
        ancPrice.setRawInputType(2)
        ancPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cursorPositionPrice = ancPrice.selectionStart
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val actualPrice = rupiahToInt(s.toString())
                    if (productEntity.price == actualPrice) {
                        return
                    }
                    productEntity.price = actualPrice
                    val parseToRupiah = parseIntToRupiah(productEntity.price)
                    ancPrice.setText(parseToRupiah)

                    // Set the cursor position to the stored position
                    ancPrice.setSelection(
                        if (cursorPositionPrice <= parseToRupiah.length) cursorPositionPrice else parseToRupiah.length
                    )
                    return
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "value must be number", Toast.LENGTH_SHORT).show()
                }
            }
        })

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveProduct.setOnClickListener {
            try {
                val qty = inpQty.text.toString().toIntOrNull() ?: return@setOnClickListener Toast.makeText(this, "invalid value quantity", Toast.LENGTH_LONG).show()
                productEntity.quantity = qty
                if (capturedImage != null) {
                    productEntity.image = "${UUID.randomUUID()}.jpeg"
                }
                productEntity.name = nPName.text.toString()
                productEntity.unit = 0
                // Get the current date and time
                val calendar = Calendar.getInstance()
                val currentDateAndTime = calendar.time
                // Define the desired date and time format
                val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                // Format the current date and time to the desired format
                productEntity.createdAt = dateFormat.format(currentDateAndTime)
                productEntity.updatedAt = dateFormat.format(currentDateAndTime)

                println(productEntity)
                // Sava data customer to room
                // Save the captured image to local storage
                // saveImageToLocalStorage(capturedImage, productEntity.image)
            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveImageToLocalStorage(bitmap: Bitmap?, uniqueFileName: String): Boolean {
        try {
            if (bitmap == null) {
                return false
            }
            val timestamp = System.currentTimeMillis()
            //Tell the media scanner about the new file so that it is immediately available to the user.
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name))
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, uniqueFileName)
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return false
                val outputStream = contentResolver.openOutputStream(uri) ?: return false
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                contentResolver.update(uri, values, null, null)
            } else {
                val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name))
                if (!imageFileFolder.exists()) {
                    imageFileFolder.mkdirs()
                }
                val imageFile = File(imageFileFolder, uniqueFileName)
                val outputStream: OutputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            capturedImage = data?.getParcelableExtra<Bitmap>("data")
            imagePrevProduct.setImageBitmap(capturedImage)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            btnTakeImgProduct.isEnabled = true
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

        return null  // This is needed to provide a return value if the cursor is null
    }

    fun getUriFromContentResolver(context: Context, fileName: String): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = context.applicationContext.contentResolver
            val queryCursor = resolver.query(
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.RELATIVE_PATH
                ),
                MediaStore.Images.Media.DISPLAY_NAME + " = ?", arrayOf(fileName), null
            )
            return if (queryCursor != null && queryCursor.moveToFirst()) {
                ContentUris.withAppendedId(
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    ),
                    queryCursor.getLong(0)
                )
            } else {
                null
            }
        } else {
            return null
        }
    }

    fun parseIntToRupiah(value: Int): String {
        val v = value.toString()
        val vp = v.chunked(1)

        if (vp.size <= 3) {
            return v
        }

        val rvp = mutableListOf<String>()
        var n = 0
        for (i in vp.size downTo 1) {
            if (n == 3) {
                n = 0
                rvp.add(".")
            }
            rvp.add(vp[i - 1])
            n++
        }

        var r = ""
        for (i in rvp.size downTo 1) {
            r += rvp[i - 1]
        }

        return r
    }

    fun rupiahToInt(rupiah: String): Int {
        // Remove non-numeric characters and the dot separator
        val cleanedString = rupiah.replace("\\D".toRegex(), "")

        if (cleanedString.isNotBlank()) {
            return cleanedString.toInt()
        }

        // Return 0 if the cleaned string is empty or contains only non-numeric characters
        return 0
    }
}

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
//        }
//
//        val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name))
//        if (!imageFileFolder.exists()) {
//            Toast.makeText(this, "Image folder not exist", Toast.LENGTH_LONG).show()
//        }
//        // 4fc0daec-77a6-4fe5-bff2-2b23c2cd13d8.jpeg Pictures/Spos
//        // content://media/external/images/media/63772
//        testImage = findViewById(R.id.cnp_name_image)
//        val file = getFileName("12345.jpeg")
//        if (file != null) {
//            Glide.with(this)
//                .load(file)  // Load the image from the specified file path
//                .into(testImage)  // Display it in your ImageView
//        }