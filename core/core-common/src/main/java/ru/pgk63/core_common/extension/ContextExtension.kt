package ru.pgk63.core_common.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri


fun Context.copyToClipboard(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("pgk",text)
    clipboard.setPrimaryClip(clip)
}

fun Context.openBrowser(url: String) {

    var validUrl = url

    if (!validUrl.startsWith("http://") && !validUrl.startsWith("https://"))
        validUrl = "https://$validUrl";

    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
    this.startActivity(browserIntent)
}