package org.wit.playlistcreater.utils

import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.wit.playlistcreater.R

fun createLoader(activity: FragmentActivity): AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true)
        .setView(R.layout.loading)
    return loaderBuilder.create()
}

fun showLoader(loader: AlertDialog) {
    if (!loader.isShowing) {
        loader.show()
    }
}

fun hideLoader(loader: AlertDialog) {
    if (loader.isShowing)
        loader.dismiss()
}

fun serviceUnavailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Music Service Unavailable. Try again later",
        Toast.LENGTH_LONG
    ).show()
}

fun serviceAvailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Music Contacted Successfully",
        Toast.LENGTH_LONG
    ).show()
}