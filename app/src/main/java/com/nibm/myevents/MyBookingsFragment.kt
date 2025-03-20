package com.nibm.myevents

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_bookings, container, false)

        val qrCodeImageView: ImageView = view.findViewById(R.id.qrCodeImageView)

        val qrText = "amod matheesha 2003"
        val bitmap = generateQRCode(qrText)

        qrCodeImageView.setImageBitmap(bitmap)

        return view
    }

    private fun generateQRCode(text: String): Bitmap {
        val writer = MultiFormatWriter()
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.MARGIN] = 1
        val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 500, 500, hints)

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}
