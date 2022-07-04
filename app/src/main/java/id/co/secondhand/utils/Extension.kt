package id.co.secondhand.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Extension {

    const val EXTRA_USER = "extra_user"

    private val timeStamp: String = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.US
    ).format(System.currentTimeMillis())

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

    private fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImage: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val file = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
        val outputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return file
    }

    fun View.dismissKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}