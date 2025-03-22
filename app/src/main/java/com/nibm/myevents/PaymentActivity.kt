package com.nibm.myevents

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Intent
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PaymentActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

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
        val eventId = intent.getStringExtra("eventId")


        eventName.text = "$name"
        totalAmount.text = "${"%.2f".format(totalPrice)} LKR"
        ticketQuantityText.text = "Ticket Quantity: $ticketQuantity"

        confirmPaymentButton.setOnClickListener {

        val user = auth.currentUser
        if (user != null) {

            val ticketData = hashMapOf(
                "eventId" to eventId,
                "ticketQuantity" to ticketQuantity,
                "dateBuy" to ServerValue.TIMESTAMP,
                "totalPrice" to totalPrice,
                "eventName" to name
            )

            val userTicketsRef = database.child("Users").child(user.uid).child("myTickets")

            val newTicketRef = userTicketsRef.push()
            newTicketRef.setValue(ticketData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Payment Successful! Ticket saved.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PaymentActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save ticket: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }
    }
}
