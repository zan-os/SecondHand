package id.co.secondhand.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

object Extension {

    const val TAG = "tag"
    const val EXTRA_USER = "extra_user"
    const val EXTRA_NOTIFICATION = "extra_notification"

    fun String.showSnackbar(view: View, context: Context, textColor: Int, backgroundColor: Int) {
        Snackbar.make(view, this, Snackbar.LENGTH_LONG)
            .setActionTextColor(textColor)
            .setBackgroundTint(
                ContextCompat.getColor(
                    context,
                    backgroundColor
                )
            )
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .show()
    }

    fun String.validateEmail(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.validatePassword(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 6
    }

    fun String.validateDescription(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 6
    }

    fun String.dateTimeFormatter(): String {
        val sdfIn = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date = sdfIn.parse(this)
        val sdfOut = SimpleDateFormat("dd MMM, HH:mm")
        return sdfOut.format(date as Date)
    }

    fun Int.currencyFormatter(): String {
        val localeId = Locale("in", "ID")
        val rupiahFormat = NumberFormat.getCurrencyInstance(localeId)
        val result = rupiahFormat.format(this)
        return result.replace("Rp", "Rp ").replace(",00", "")
    }

    fun View.dismissKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}