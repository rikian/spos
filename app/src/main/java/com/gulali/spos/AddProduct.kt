package com.gulali.spos

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.gulali.spos.helper.NoEnterInputFilter

class AddProduct: ComponentActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var nPName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

        btnBack = findViewById(R.id.btn_back_tbc)
        nPName = findViewById(R.id.anp_edt_name)
        nPName.filters = arrayOf(NoEnterInputFilter())
        btnBack.setOnClickListener {
            finish()
        }
    }
}