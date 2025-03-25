package com.nibm.myevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import android.widget.Button
import android.content.Intent
import android.content.SharedPreferences
import android.content.Context

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var profileImageView: ImageView
    private lateinit var nameEditText: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var saveProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        profileImageView = view.findViewById(R.id.profileImage)
        nameEditText = view.findViewById(R.id.nameEditText)
        emailTextView = view.findViewById(R.id.emailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)
        saveProfileButton = view.findViewById(R.id.save_profile_button)

        loadUserProfile()

        saveProfileButton.setOnClickListener {
            saveProfileChanges()
        }

        logoutButton.setOnClickListener {
            auth.signOut()

            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            database.child("Users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                        nameEditText.text = name ?: "Name not available"
                        emailTextView.text = email ?: "Email not available"

                        if (!profileImageUrl.isNullOrEmpty()) {
                            Picasso.get().load(profileImageUrl).into(profileImageView)
                        } else {
                            profileImageView.setImageResource(R.drawable.sample_profile)
                        }
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load profile: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun saveProfileChanges() {
        val user = auth.currentUser
        val userId = user?.uid
        val newName = nameEditText.text.toString().trim()

        if (userId != null && newName.isNotEmpty()) {
            database.child("Users").child(userId).child("name").setValue(newName)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(requireContext(), "Failed to update profile: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}

