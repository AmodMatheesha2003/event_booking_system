package com.nibm.myevents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class VerifyTicketsFragment : Fragment() {

    private lateinit var scanButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify_tickets, container, false)

        scanButton = view.findViewById(R.id.scanButton)
        resultTextView = view.findViewById(R.id.resultTextView)

        scanButton.setOnClickListener {
            startQRScanner()
        }

        return view
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
                resultTextView.text = "Scanned QR Code: ${result.contents}"
            } else {
                resultTextView.text = "Scan cancelled"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
