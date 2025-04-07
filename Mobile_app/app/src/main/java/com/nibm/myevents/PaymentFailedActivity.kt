package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PaymentFailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_failed)

        val retryerrorButton = findViewById<Button>(R.id.retryerrorButton)

        retryerrorButton.setOnClickListener {
            finish()
        }
    }
}
