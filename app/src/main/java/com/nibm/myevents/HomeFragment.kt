package com.nibm.myevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<Event>
    private lateinit var adapter: EventAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var greetingText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        greetingText = view.findViewById(R.id.greetingText)

        loadUserName()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventList = ArrayList()
        adapter = EventAdapter(eventList)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("eventForm")
        fetchEvents()

        return view
    }

    private fun loadUserName() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            database.child("Users").child(userId).child("name").get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.value as? String ?: "Guest"
                    greetingText.text = "Hi, $name 👋"
                }
                .addOnFailureListener {
                    greetingText.text = "Hi, Guest 👋"
                }
        } else {
            greetingText.text = "Hi, Guest 👋"
        }
    }

    private fun fetchEvents() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    if (event != null) {
                        eventList.add(event)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
