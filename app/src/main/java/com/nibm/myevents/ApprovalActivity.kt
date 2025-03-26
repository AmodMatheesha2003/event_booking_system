package com.nibm.myevents

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ApprovalActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_verified)

        val eventStatusText: TextView = findViewById(R.id.eventstatus)
        val continueButton: Button = findViewById(R.id.qrcontinueButton)
        val goBackButton: Button = findViewById(R.id.GoBack)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val ticketInfo = intent.getStringExtra("TICKET_INFO") ?: "No Ticket Data"
        val eventId = intent.getStringExtra("TICKET_eventId")

        eventStatusText.text = ticketInfo

        goBackButton.setOnClickListener {
            finish()
        }

        continueButton.setOnClickListener {
            val userId = auth.currentUser?.uid

            if (userId != null && eventId != null) {
                val myTicketsRef = database.reference.child("Users").child(userId).child("myTickets")

                myTicketsRef.orderByChild("eventId").equalTo(eventId).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        for (ticketSnapshot in snapshot.children) {
                            val ticketKey = ticketSnapshot.key
                            ticketKey?.let {
                                myTicketsRef.child(it).removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Check In successfully!", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to Check In .", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Ticket not found.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error fetching ticket data.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not logged in or event ID missing.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
