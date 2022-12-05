package com.linhphan.presentation.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.linhphan.common.Logger
import java.io.File


fun View.temporaryLockView(time: Long = 200): Runnable {
    isEnabled = false
    val runnable = Runnable { isEnabled = true }
    postDelayed(runnable, time)
    return runnable
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.toast(@StringRes mesResId: Int, duration: Int = Toast.LENGTH_SHORT){
    context.toast(context.getString(mesResId), duration)
}

fun View.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    context.toast(message, duration)
}

fun View.enableTapToHideKeyboard() {
    this.setOnTouchListener { v, _ ->
        hideKeyboard()
        true
    }
}

fun View.hideKeyboard(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

////////////////////////////
fun ViewGroup.inflate(@LayoutRes l: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(l, this, attachToRoot)

////////////////////////////
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Context.toastLong(message: String, duration: Int = Toast.LENGTH_LONG)
        = toast(message, duration)

////////////////////////////
/**
 * Checks if the device is rooted.
 * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
 */
fun Context.isRootedDevice(): Boolean {
    // get from build info
    val isEmulator = isEmulator()
    val buildTags = Build.TAGS
    if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
        return true
    }

    // check if /system/app/Superuser.apk is present
    try{
        var file = File("/system/app/Superuser.apk")
        if (file.exists()) {
            return true
        } else {
            file = File ("/system/xbin/su")
            !isEmulator && file.exists()
        }
    }catch (e: Exception){
        // ignore
    }

    return executeShellCommand("su")

    // try executing commands
//    return canExecuteCommand("/system/xbin/which su")
//            || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su")
}

@SuppressLint("HardwareIds")
fun Context.isEmulator(): Boolean {
    var androidId: String? = null
    try {
        androidId = Settings.Secure.getString(contentResolver, "android_id")
    }catch (e: Exception){
        Logger.e("isEmulator", e.message, e)
    }
    return androidId == null
            || "sdk" == Build.PRODUCT || "google_sdk" == Build.PRODUCT
            || "sdk_gphone_x86" == Build.PRODUCT || "generic_x86" == Build.DEVICE
}

// executes a command on the system
private fun canExecuteCommand(command: String): Boolean {
    return try {
        Runtime.getRuntime().exec(command)
        true
    } catch (e: java.lang.Exception) {
        false
    }
}

private fun executeShellCommand(su: String): Boolean {
    var process: Process? = null
    var isRooted: Boolean = false
    try {
        process = Runtime.getRuntime().exec(su)
        isRooted = true
    } catch (e: Exception) {
    } finally {
        if (process != null) {
            try {
                process.destroy();
            } catch (e: Exception) {

            }
        }
    }
    return isRooted
}

////////////////////////////
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = Transformations.distinctUntilChanged(this)