package com.nibm.myevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(private val eventList: ArrayList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.eventName)
        val eventDetails: TextView = view.findViewById(R.id.eventDetails)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val eventTime: TextView = view.findViewById(R.id.eventTime)
        val eventLocation: TextView = view.findViewById(R.id.eventLocation)
        val ticketPrice: TextView = view.findViewById(R.id.ticketPrice)
        val eventImage: ImageView = view.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.eventName
        holder.eventDetails.text = event.eventDetails
        holder.eventDate.text = "Date: ${event.date}"
        holder.eventTime.text = "Time: ${event.stime} - ${event.etime}"
        holder.eventLocation.text = "Location: ${event.location}"
        holder.ticketPrice.text = "Price: ${event.ticketPrice} LKR"


        Glide.with(holder.itemView.context)
            .load(event.imageUrl)
            .into(holder.eventImage)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
