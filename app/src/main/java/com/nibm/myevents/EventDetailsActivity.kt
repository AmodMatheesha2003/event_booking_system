package com.nibm.myevents

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.widget.ImageButton
import android.widget.Button
import android.content.Intent

class EventDetailsActivity : AppCompatActivity() {
    private var quantity: Int = 1
    private lateinit var ticketQuantity: TextView
    private lateinit var totalPriceText: TextView
    private var ticketPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventName: TextView = findViewById(R.id.eventName)
        val eventDetails: TextView = findViewById(R.id.eventDetails)
        val eventDate: TextView = findViewById(R.id.eventDate)
        val eventTime: TextView = findViewById(R.id.eventTime)
        val eventLocation: TextView = findViewById(R.id.eventLocation)
        val ticketPriceText: TextView = findViewById(R.id.ticketPrice)
        val ticketAmount: TextView = findViewById(R.id.ticketAmount)
        val eventImage: ImageView = findViewById(R.id.eventImage)
        val payButton: Button = findViewById(R.id.payButton)

        val name = intent.getStringExtra("eventName")
        val details = intent.getStringExtra("eventDetails")
        val date = intent.getStringExtra("eventDate")
        val time = intent.getStringExtra("eventTime")
        val location = intent.getStringExtra("eventLocation")
        val price = intent.getStringExtra("ticketPrice")
        val amount = intent.getStringExtra("ticketAmount")
        val imageUrl = intent.getStringExtra("imageUrl")
        val eventId = intent.getStringExtra("eventId")

        val goBackButton: ImageView = findViewById(R.id.goBackButton)

        eventName.text = name
        eventDetails.text = details
        eventDate.text = "Date: $date"
        eventTime.text = "Time: $time"
        eventLocation.text = "Location: $location"
        ticketPriceText.text = "Price: $price LKR"
        ticketAmount.text = "$amount"

        ticketPrice = price?.toDoubleOrNull() ?: 0.0

        Glide.with(this)
            .load(imageUrl)
            .into(eventImage)

        goBackButton.setOnClickListener {
            finish()
        }

        ticketQuantity = findViewById(R.id.ticketQuantity)
        totalPriceText = findViewById(R.id.totalPrice)

        val incrementButton: ImageButton = findViewById(R.id.incrementButton)
        val decrementButton: ImageButton = findViewById(R.id.decrementButton)

        incrementButton.setOnClickListener {
            if (quantity < 6) {
                quantity++
                updateQuantity()
            }
        }

        decrementButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantity()
            }
        }

        updateQuantity()

         payButton.setOnClickListener {
            val totalPrice = ticketPrice * quantity
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("eventName", name)
                putExtra("totalPrice", totalPrice)
                putExtra("ticketQuantity", quantity)
                putExtra("eventId", eventId)
            }
            startActivity(intent)
        }

    }

    private fun updateQuantity() {
        ticketQuantity.text = quantity.toString()
        val totalPrice = ticketPrice * quantity
        totalPriceText.text = "Total Price: ${"%.2f".format(totalPrice)} LKR"
    }
}
