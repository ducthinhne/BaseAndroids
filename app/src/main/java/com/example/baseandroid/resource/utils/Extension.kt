package com.example.baseandroid.resource.utils

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.baseandroid.application.base.MyActivity
import com.example.baseandroid.application.base.MyFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun MyFragment.delay(
    timeMillis: Long, execute: () -> Unit
) {
    fragmentScope.launch {
        kotlinx.coroutines.delay(timeMillis)
        execute()
    }
}

fun MyActivity.delay(
    timeMillis: Long, execute: () -> Unit
) {
    activityScope.launch {
        kotlinx.coroutines.delay(timeMillis)
        execute()
    }
}

fun View.stopAnimation() {
//    this.animation.cancel()
}

fun View.visible() {
    this.visibility = View.VISIBLE
    this.isEnabled = true
}

fun View.hidden() {
    this.visibility = View.INVISIBLE
    this.isEnabled = false
}

fun View.gone() {
    this.visibility = View.GONE
    this.isEnabled = false
}

val GOOGLE_MAP_STYLE = """
    [
      {
        "featureType": "administrative.land_parcel",
        "elementType": "labels",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "labels.text",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "poi.business",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "labels.icon",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "road.local",
        "elementType": "labels",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "transit",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      }
    ]
""".trimIndent()

fun View.animeFade(isShow: Boolean, duration: Long = 0) {
    if (isShow == isVisible) {
        return
    }
    val toAlpha = if (isShow) 1f else 0f
    this.visible()
    this.alpha = if (isShow) 0f else 1f
    animate()
        .alpha(toAlpha)
        .setDuration(duration)
        .setComplete { if (isShow) visible() else gone() }
        .start()
}

fun Context.showSingleActionAlert(
    title: String, message: String,
    actionTitle: String = "OK",
    completion: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(actionTitle) { _, _ ->
            completion()
        }
        .setCancelable(false)
        .create()
        .apply {
            setCanceledOnTouchOutside(false)
            show()
        }
}

fun Context.showTwoActionAlert(
    title: String, message: String,
    positiveTitle: String = "OK",
    negativeTitle: String = "Cancel",
    positiveAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null
) {
    CoroutineScope(Dispatchers.Main).launch {
        AlertDialog.Builder(this@showTwoActionAlert)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveTitle) { _, _ ->
                positiveAction?.let { it() }
            }
            .setNegativeButton(negativeTitle) { _, _ ->
                negativeAction?.let { it() }
            }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}

suspend fun Context.showTwoActionAlert(
    title: String, message: String,
    positiveTitle: String = "OK",
    negativeTitle: String = "Cancel"
) = suspendCoroutine<Boolean> { continuation ->
    showTwoActionAlert(title, message, positiveTitle, negativeTitle, positiveAction = {
        continuation.resume(true)
    })
}

fun View.jumping(translationY: Float = 20F, duration: Long, loop: Boolean = true) {
    animate()
        .translationY(translationY)
        .setDuration(duration / 2)
        .setComplete {
            animate()
                .translationY(-translationY)
                .setDuration(1500L)
                .setComplete {
                    if (loop) {
                        jumping(translationY, duration, loop)
                    }
                }
        }
}

fun View.toggleSelected() {
    isSelected = !isSelected
}

fun View.togleVisible() {
    if (isVisible) {
        gone()
    } else {
        visible()
    }
}

fun View.animeRotate(rotation: Float) {
    animate()
        .rotation(rotation)
        .setDuration(200)
        .start()
}

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.string(pattern: String = "yyyy-MM-dd HH:mm"): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

fun Context.hasPermissions(permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun MyFragment.checkPermission(
    perms: Array<String>
) {
    if (requireContext().hasPermissions(perms)) {
        onPermissionGranted()
    } else {
        permissionsResult.launch(perms)
    }
}

fun ViewPropertyAnimator.setComplete(completion: (Animator?) -> Unit): ViewPropertyAnimator {
    return setListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(p0: Animator) {

        }

        override fun onAnimationEnd(p0: Animator) {
            completion(p0)
        }

        override fun onAnimationCancel(p0: Animator) {

        }

        override fun onAnimationRepeat(p0: Animator) {

        }
    })
}

fun View.setPaddingAsDP(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    setPadding(asPixels(left), asPixels(top), asPixels(right), asPixels(bottom))
}

fun View.asPixels(value: Int): Int {
    val scale = resources.displayMetrics.density
    val dpAsPixels = (value * scale + 0.5f)
    return dpAsPixels.toInt()
}