package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import android.widget.Toast

class VerifyTicketsFragment : Fragment() {

    private lateinit var scanButton: Button
    private lateinit var resultTextView: TextView

    private lateinit var eventsSpinner: Spinner
    private lateinit var database: DatabaseReference
    private val eventList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify_tickets, container, false)

        scanButton = view.findViewById(R.id.scanButton)
        resultTextView = view.findViewById(R.id.resultTextView)
        eventsSpinner = view.findViewById(R.id.eventsSpinner)

        database = FirebaseDatabase.getInstance().reference

        loadEvents()

        scanButton.setOnClickListener {
            val selectedEvent = eventsSpinner.selectedItem.toString()

            if (selectedEvent == "Select Event") {
                Toast.makeText(requireContext(), "Please select an event first!", Toast.LENGTH_SHORT).show()
            } else {
                startQRScanner()
            }
        }

        return view
    }

    private fun loadEvents() {
        database.child("eventForm").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventNames = mutableListOf<String>().apply {
                    add("Select Event")
                }

                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    event?.let {
                        eventList.add(it)
                        if (!it.eventName.isNullOrEmpty()) {
                            eventNames.add(it.eventName!!)
                        }
                    }
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    eventNames
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                eventsSpinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load events: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startQRScanner() {
        IntentIntegrator.forSupportFragment(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Scan a QR Code")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                handleScanResult(result.contents)
            } else {
                resultTextView.text = "Scan cancelled"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleScanResult(contents: String) {
        val verificationText = """
            $contents
        """.trimIndent()
        
        resultTextView.text = verificationText
    }
}