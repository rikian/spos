package com.gulali.spos

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.gulali.spos.database.CustomerDB
import com.gulali.spos.database.CustomerEntity
import com.gulali.spos.database.ProductEntity
import com.gulali.spos.helper.NoEnterInputFilter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AddCustomer: ComponentActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var nCName: EditText
    private lateinit var nCPhone: EditText
    private lateinit var nCImage: ImageButton
    private lateinit var nCImagePreview: ImageView
    private lateinit var btnSave: Button

    // Variable to hold the captured image
    private var capturedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_customer)
        btnBack = findViewById(R.id.btn_back_tbc)
        nCName = findViewById(R.id.anc_edt_name)
        nCName.filters = arrayOf(NoEnterInputFilter())
        nCPhone = findViewById(R.id.anc_edt_phone)
        nCPhone.filters = arrayOf(NoEnterInputFilter())
        nCImage = findViewById(R.id.btn_take_img_product)
        nCImagePreview = findViewById(R.id.img_prev_product)
        btnSave = findViewById(R.id.btn_save_nc)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
            nCImage.isEnabled = true
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }

        // Set an OnClickListener for the nCImage button to open the camera
        nCImage.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }

        // Set an OnClickListener for the btnSave button
//        btnSave.setOnClickListener {
//            if (capturedImage != null) {
//                val uniqueFilename = "${UUID.randomUUID()}.jpeg"
//                // Sava data customer to room
//                // Save the captured image to local storage
//                saveImageToLocalStorage(capturedImage, uniqueFilename)
//                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "No image to save", Toast.LENGTH_LONG).show()
//            }
//        }

        btnSave.setOnClickListener {
            // Toast.makeText(this, "Save Customer", Toast.LENGTH_LONG).show()
            // save customer to DBroom
            val db = CustomerDB.getCustomerDatabase(applicationContext)
            val cusDao = db.customerDAO()
            val customer = CustomerEntity(
                name = nCName.text.toString(),
                phone = nCPhone.text.toString(),
                createdAt = "12-12-12",
                updatedAt = "12-12-12",
            )
            cusDao.insertCustomer(customer)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            capturedImage = data?.getParcelableExtra<Bitmap>("data")
            nCImagePreview.setImageBitmap(capturedImage)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            nCImage.isEnabled = true
        }
    }

    // Function to save the image to local storage
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
}

//private fun saveImageToStorage(image: Bitmap?) {
//    if (image != null) {
//        val uniqueFilename = "${UUID.randomUUID()}.jpg" // Generates a unique filename
//        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        val file = File(path, uniqueFilename)
//
//        try {
//            val outputStream = FileOutputStream(file)
//            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.close()
//
//            // Notify the media scanner about the new file
//            MediaScannerConnection.scanFile(
//                this,
//                arrayOf(file.absolutePath),
//                null,
//                null
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}