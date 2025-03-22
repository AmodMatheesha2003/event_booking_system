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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_bookings, container, false)

        val qrCodeImageView: ImageView = view.findViewById(R.id.qrCodeImageView)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser
        if (user != null) {
            loadUserTickets(user.uid, qrCodeImageView)
        } else {
            generateDefaultQR(qrCodeImageView)
        }

        return view
    }

    private fun loadUserTickets(userId: String, imageView: ImageView) {
        database.child("Users").child(userId).child("myTickets")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tickets = StringBuilder()
                    tickets.append("UID: $userId\n\n")
                    if (snapshot.exists()) {
                        for (ticketSnapshot in snapshot.children) {
                            val ticket = ticketSnapshot.getValue(Ticket::class.java)
                            ticket?.let {
                                tickets.append("EID: ${it.eventId}\n")
                                tickets.append("Date: ${formatDate(it.dateBuy)}\n")
                                tickets.append("Quantity: ${it.ticketQuantity}\n\n")
                            }
                        }
                    } else {
                        tickets.append("No purchased events")
                    }

                    val bitmap = generateQRCode(tickets.toString())
                    imageView.setImageBitmap(bitmap)
                }

                override fun onCancelled(error: DatabaseError) {
                    generateDefaultQR(imageView)
                }
            })
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    private fun generateDefaultQR(imageView: ImageView) {
        val bitmap = generateQRCode("No any events")
        imageView.setImageBitmap(bitmap)
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
