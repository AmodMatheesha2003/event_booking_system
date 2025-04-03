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
import java.util.Random

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var otp: String = ""

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

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter the name!", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter the email address", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            } else {
                generateOtp()
                sendOtpToEmail(email, name)
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("userName", name)
                intent.putExtra("userEmail", email)
                intent.putExtra("userPassword", password)
                intent.putExtra("generatedOtp", otp)
                startActivity(intent)
            }
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateOtp() {
        otp = (1000..9999).random().toString()
    }

    private fun sendOtpToEmail(email: String, userName: String) {
        val subject = "Account Verification - OTP Code"
        val messageBody = """
            Dear $userName,

            Thank you for signing up with MyEvents. To complete your registration, please use the One-Time Password (OTP) provided below to verify your account:

            OTP Code: $otp

            This code will expire in 10 minutes. Please do not share it with anyone. If you did not request this verification, please ignore this email.

            Regards,
            The MyEvents Team
            """.trimIndent()
        val javaMailAPI = JavaMailAPI(email, subject, messageBody)
        javaMailAPI.execute()
        Toast.makeText(this, "OTP email sent! Please check your inbox.", Toast.LENGTH_LONG).show()
    }
}
