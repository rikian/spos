package com.gulali.spos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gulali.spos.databinding.BottomSheetBinding

open class BottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: BottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseBtmenu.setOnClickListener {
            dismiss()
        }

        binding.addCustomer.setOnClickListener {
            val addCustomerIntent = Intent(context, AddCustomer::class.java)
            startActivity(addCustomerIntent)
        }

        binding.addProduct.setOnClickListener {
            val intent = Intent(context, AddProduct::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}