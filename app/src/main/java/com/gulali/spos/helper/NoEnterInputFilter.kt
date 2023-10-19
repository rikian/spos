package com.gulali.spos.helper

import android.text.InputFilter
import android.text.Spanned

class NoEnterInputFilter: InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // Disable the "Enter" key by replacing it with an empty string
        return source?.toString()?.replace("\n", "")
    }
}