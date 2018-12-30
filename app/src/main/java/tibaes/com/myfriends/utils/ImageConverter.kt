package tibaes.com.myfriends.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import android.view.ViewGroup
import android.graphics.BitmapFactory




fun imgToByteArray(image: ImageView) : ByteArray {

    val bitmap = (image.drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 45, stream)

    return stream.toByteArray()
}

fun byteToImg(bytes: ByteArray): Bitmap{
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}