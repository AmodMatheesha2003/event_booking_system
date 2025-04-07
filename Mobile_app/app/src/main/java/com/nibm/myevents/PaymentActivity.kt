package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dbRef = database.reference

        val eventName: TextView = findViewById(R.id.paymentEventName)
        val totalAmount: TextView = findViewById(R.id.paymentTotalAmount)
        val ticketQuantityText: TextView = findViewById(R.id.paymentTicketQuantity)
        val confirmPaymentButton: Button = findViewById(R.id.payButton)
        val goBackButton: ImageView = findViewById(R.id.goBackButtonpayement)

        goBackButton.setOnClickListener { finish() }

        val name = intent.getStringExtra("eventName")
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val ticketQuantity = intent.getIntExtra("ticketQuantity", 1)
        val eventId = intent.getStringExtra("eventId")

        eventName.text = name
        totalAmount.text = "${"%.2f".format(totalPrice)} LKR"
        ticketQuantityText.text = "Ticket Quantity: $ticketQuantity"

        confirmPaymentButton.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            if (user != null) {
                val userId = user.uid
                val userEmail = user.email ?: ""
                val userRef = dbRef.child("Users").child(userId)

                userRef.get().addOnSuccessListener { snapshot ->
                    val userName = snapshot.child("name").getValue(String::class.java) ?: "Valued Customer"
                    val timestamp = System.currentTimeMillis()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormat.format(Date(timestamp))

                    val ticketData = hashMapOf(
                        "eventId" to eventId,
                        "ticketQuantity" to ticketQuantity,
                        "dateBuy" to formattedDate,
                        "totalPrice" to totalPrice,
                        "eventName" to name
                    )

                    val userTicketsRef = dbRef.child("Users").child(userId).child("myTickets")
                    val newTicketRef = userTicketsRef.push()

                    newTicketRef.setValue(ticketData).addOnSuccessListener {
                        if (!eventId.isNullOrEmpty()) {
                            val eventRef = dbRef.child("eventForm").child(eventId)
                            eventRef.child("ticketAmount").get().addOnSuccessListener { amountSnapshot ->
                                val currentAmountStr = amountSnapshot.getValue(String::class.java) ?: "0"
                                val currentAmount = currentAmountStr.toIntOrNull() ?: 0
                                val newAmount = currentAmount - ticketQuantity

                                if (newAmount >= 0) {
                                    eventRef.child("ticketAmount").setValue(newAmount.toString())
                                        .addOnSuccessListener {
                                              val incomeData = hashMapOf(
                                                    "eventId" to eventId,
                                                    "customerId" to userId,
                                                    "ticketQuantity" to ticketQuantity,
                                                    "dateOfPurchase" to formattedDate,
                                                    "totalPrice" to totalPrice
                                                )

                                                dbRef.child("Income").push()
                                                    .setValue(incomeData)
                                                    .addOnSuccessListener {
                                                        sendConfirmationEmail(userEmail, userName, name, ticketQuantity, totalPrice, formattedDate)
                                                        navigateToSuccess()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        showError("Failed to record income: ${e.message}")
                                                    }
                                        }
                                        .addOnFailureListener { e ->
                                            showError("Failed to update tickets: ${e.message}")
                                        }
                                } else {
                                    showError("Not enough tickets available")
                                }
                            }.addOnFailureListener { e ->
                                showError("Error fetching tickets: ${e.message}")
                            }
                        } else {
                            showError("Invalid event ID")
                        }
                    }.addOnFailureListener { e ->
                        showError("Failed to save ticket: ${e.message}")
                    }
                }.addOnFailureListener { e ->
                    showError("Failed to get user data: ${e.message}")
                }
            } else {
                showError("User not authenticated!")
            }
        }
    }

    private fun sendConfirmationEmail(
        email: String,
        name: String,
        eventName: String?,
        quantity: Int,
        totalPrice: Double,
        date: String
    ) {
        val subject = "Payment Confirmation - Your Ticket is Confirmed!"
        val message = """
            Dear $name,

            We are pleased to inform you that your ticket purchase has been successfully processed.

            Event Details
            Event Name: $eventName
            Ticket Quantity: $quantity
            Total Price: ${"%.2f".format(totalPrice)} LKR
            Purchase Date: $date

            Thank you for choosing us!

            Best Regards,
            MyEvents Team
        """.trimIndent()

        JavaMailAPI(email, subject, message).execute()
    }

    private fun navigateToSuccess() {
        val intent = Intent(this@PaymentActivity, PaymentSuccessfulActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val intent = Intent(this@PaymentActivity, PaymentFailedActivity::class.java)
        startActivity(intent)
        finish()
    }
}