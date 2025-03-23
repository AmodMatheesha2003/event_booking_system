package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ApprovalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_verified)

        val eventStatusText: TextView = findViewById(R.id.eventstatus)
        val continueButton: Button = findViewById(R.id.qrcontinueButton)

        val ticketInfo = intent.getStringExtra("TICKET_INFO") ?: "No Ticket Data"

        eventStatusText.text = ticketInfo

        continueButton.setOnClickListener {
            finish()
        }
    }
}
