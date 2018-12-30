package tibaes.com.myfriends.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.makePhoneCall(number: String) : Boolean {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

fun Context.sendEmail(email: String) : Boolean {
    try {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

fun Context.goSite(url: String) : Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }

    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
}

fun Context.sendSMS(phone: String) : Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null))
        startActivity(intent)

        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}