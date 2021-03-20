package com.example.telega.telega.utilits

import android.text.Editable
import android.text.TextWatcher

class AppTextWatcher(val OnSuccess:(Editable?) -> Unit): TextWatcher {
    override fun afterTextChanged(s: Editable?)
    {
        OnSuccess(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}