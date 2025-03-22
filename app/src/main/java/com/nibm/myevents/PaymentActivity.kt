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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                val userId = user.uid
                val userEmail = user.email ?: ""

                // Reference to the user's data in Firebase
                val userRef = database.child("Users").child(userId)

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

                    val userTicketsRef = database.child("Users").child(user.uid).child("myTickets")
                    val newTicketRef = userTicketsRef.push()

                    newTicketRef.setValue(ticketData)
                        .addOnSuccessListener {
                            val subject = "Payment Confirmation - Your Ticket is Confirmed!"
                            val message = """
                                Dear $userName,

                                We are pleased to inform you that your ticket purchase has been successfully processed. Please find your ticket details below:

                                Event Details
                                Event Name: $name
                                Ticket Quantity: $ticketQuantity
                                Total Price: ${"%.2f".format(totalPrice)} LKR
                                Purchase Date: $formattedDate

                                Your ticket(s) are now confirmed, and we look forward to welcoming you to the event. If you have any questions or require further assistance, please do not hesitate to contact us.
                                Thank you for choosing us!

                                Best Regards,
                                MyEvents Team
                            """.trimIndent()

                            JavaMailAPI(userEmail, subject, message).execute()

                            val intent = Intent(this@PaymentActivity, PaymentSuccessfulActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save ticket: ${it.message}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@PaymentActivity, PaymentFailedActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve user name", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
