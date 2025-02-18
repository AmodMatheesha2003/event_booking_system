package com.nibm.myevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

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
}
