package com.nibm.myevents

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(private val eventList: ArrayList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.eventName)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val ticketPrice: TextView = view.findViewById(R.id.ticketPrice)
        val eventImage: ImageView = view.findViewById(R.id.eventImage)
        val seeMoreButton: Button = view.findViewById(R.id.seeMoreButton)
        val eventLocation: TextView = view.findViewById(R.id.eventLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.eventName
        holder.eventDate.text = "Date: ${event.date}"
        holder.eventLocation.text = "Location: ${event.location}"
        holder.ticketPrice.text = "Price: ${event.ticketPrice} LKR"

        Glide.with(holder.itemView.context)
            .load(event.imageUrl)
            .into(holder.eventImage)

        holder.seeMoreButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventDetailsActivity::class.java).apply {
                putExtra("eventName", event.eventName)
                putExtra("eventDetails", event.eventDetails)
                putExtra("eventDate", event.date)
                putExtra("eventTime", "${event.stime} - ${event.etime}")
                putExtra("eventLocation", event.location)
                putExtra("ticketPrice", event.ticketPrice)
                putExtra("imageUrl", event.imageUrl)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
