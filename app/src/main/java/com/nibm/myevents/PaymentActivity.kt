package com.nibm.myevents

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Intent
import android.widget.ImageView

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val eventName: TextView = findViewById(R.id.paymentEventName)
        val totalAmount: TextView = findViewById(R.id.paymentTotalAmount)
        val ticketQuantityText: TextView = findViewById(R.id.paymentTicketQuantity)
        val confirmPaymentButton: Button = findViewById(R.id.payButton)

        val goBackButton: ImageView = findViewById(R.id.goBackButtonpayement)
        goBackButton.setOnClickListener {
            finish()
        }

        val name = intent.getStringExtra("eventName")
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val ticketQuantity = intent.getIntExtra("ticketQuantity", 1)

        eventName.text = "$name"
        totalAmount.text = "${"%.2f".format(totalPrice)} LKR"
        ticketQuantityText.text = "Ticket Quantity: $ticketQuantity"

        confirmPaymentButton.setOnClickListener {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@PaymentActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
