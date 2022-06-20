package id.co.secondhand.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object DismissKeyboard {

    fun View.dismissKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}