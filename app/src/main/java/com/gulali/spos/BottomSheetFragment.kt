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

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//
//        // Set the flags to overlay the navigation
//        dialog.window?.apply {
//            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
//            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }
//
//        return dialog
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val color = Color.WHITE
//        val isLite = true
//        dialog?.window?.run {
//            navigationBarColor = color
//            WindowCompat.getInsetsController(this, this.decorView).isAppearanceLightNavigationBars = isLite
//        }
        // Add your bottom sheet logic here
        // For example, you can set click listeners for the items in the bottom sheet
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