package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DeclineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_invalid)

        val tryAgainButton: Button = findViewById(R.id.qrtryAgainButton)

        tryAgainButton.setOnClickListener {
            finish()
        }
    }
}
