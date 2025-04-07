package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OTPActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        val otpEditText = findViewById<EditText>(R.id.otpEditText)
        val verifyButton = findViewById<Button>(R.id.verifyButton)
        val backsign = findViewById<TextView>(R.id.backsign)

        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")
        val userPassword = intent.getStringExtra("userPassword")
        val generatedOtp = intent.getStringExtra("generatedOtp")

        verifyButton.setOnClickListener {
            val enteredOtp = otpEditText.text.toString().trim()

            if (enteredOtp.isEmpty()) {
                Toast.makeText(this, "Please enter the OTP!", Toast.LENGTH_SHORT).show()
            } else if (enteredOtp == generatedOtp) {
                createUserAccount(userName, userEmail, userPassword)
            } else {
                Toast.makeText(this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        backsign.setOnClickListener { finish() }

    }

    private fun createUserAccount(name: String?, email: String?, password: String?) {
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserToDatabase(userId, name!!, email)
                    }
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(userId: String, name: String, email: String) {
        val user = User(userId, name, email)

        database.child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error saving user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
