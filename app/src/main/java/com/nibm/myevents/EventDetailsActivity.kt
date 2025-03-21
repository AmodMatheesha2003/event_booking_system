package com.nibm.myevents

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventName: TextView = findViewById(R.id.eventName)
        val eventDetails: TextView = findViewById(R.id.eventDetails)
        val eventDate: TextView = findViewById(R.id.eventDate)
        val eventTime: TextView = findViewById(R.id.eventTime)
        val eventLocation: TextView = findViewById(R.id.eventLocation)
        val ticketPrice: TextView = findViewById(R.id.ticketPrice)
        val eventImage: ImageView = findViewById(R.id.eventImage)

        val name = intent.getStringExtra("eventName")
        val details = intent.getStringExtra("eventDetails")
        val date = intent.getStringExtra("eventDate")
        val time = intent.getStringExtra("eventTime")
        val location = intent.getStringExtra("eventLocation")
        val price = intent.getStringExtra("ticketPrice")
        val imageUrl = intent.getStringExtra("imageUrl")

        eventName.text = name
        eventDetails.text = details
        eventDate.text = "Date: $date"
        eventTime.text = "Time: $time"
        eventLocation.text = "Location: $location"
        ticketPrice.text = "Price: $price LKR"

        Glide.with(this)
            .load(imageUrl)
            .into(eventImage)
    }
}
