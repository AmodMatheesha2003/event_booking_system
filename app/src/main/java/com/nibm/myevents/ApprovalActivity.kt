package com.nibm.myevents

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class ApprovalActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_verified)

        val eventStatusText: TextView = findViewById(R.id.eventstatus)
        val continueButton: Button = findViewById(R.id.qrcontinueButton)
        val goBackButton: Button = findViewById(R.id.GoBack)

        database = FirebaseDatabase.getInstance()

        val ticketInfo = intent.getStringExtra("TICKET_INFO") ?: "No Ticket Data"
        val eventId = intent.getStringExtra("TICKET_eventId")
        val userId = intent.getStringExtra("TICKET_userId")

        eventStatusText.text = ticketInfo

        goBackButton.setOnClickListener {
            finish()
        }

        continueButton.setOnClickListener {
            if (userId != null && eventId != null) {
                val myTicketsRef = database.reference.child("Users").child(userId).child("myTickets")

                myTicketsRef.get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        var ticketFound = false
                        for (ticketSnapshot in snapshot.children) {
                            val ticketEventId = ticketSnapshot.child("eventId").value.toString()
                            Log.d("ApprovalActivity", "Checking ticket: $ticketEventId")
                            if (ticketEventId == eventId) {
                                ticketFound = true
                                val ticketKey = ticketSnapshot.key
                                ticketKey?.let {
                                    myTicketsRef.child(it).removeValue().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Check-In successful!", Toast.LENGTH_SHORT).show()
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Failed to Check-In.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                        if (!ticketFound) {
                            Toast.makeText(this, "Ticket not found.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No tickets found for user.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching ticket data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "User ID or Event ID missing.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
