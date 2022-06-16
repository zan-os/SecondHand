package id.co.secondhand.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

object Extension {

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

    fun String.validatePassword(): Boolean {
        return this.length >= 6
    }

    fun String.validateEmail(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}