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

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        val nameEditText = findViewById<EditText>(R.id.signup_name)
        val emailEditText = findViewById<EditText>(R.id.signup_email)
        val passwordEditText = findViewById<EditText>(R.id.signup_password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.signup_Confirm_password)
        val signupButton = findViewById<Button>(R.id.signup_button)
        val loginRedirectText = findViewById<TextView>(R.id.loginRedirectText)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (!NetworkUtils.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter the name!", Toast.LENGTH_SHORT).show()
            }else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter the email address", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show()
            } else if(password.length <= 6){
                Toast.makeText(this, "Password should contain more than 6 characters", Toast.LENGTH_SHORT).show()
            } else if(password != confirmPassword){
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }else {
                registerUser(name, email, password)
            }
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserToDatabase(userId, name, email)
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
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error saving user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}