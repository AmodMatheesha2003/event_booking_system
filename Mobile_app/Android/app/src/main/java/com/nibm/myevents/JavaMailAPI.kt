package com.nibm.myevents

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties
import android.os.AsyncTask

class JavaMailAPI(
    private val recipientEmail: String,
    private val subject: String,
    private val messageBody: String
) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        return try {
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.port"] = "587"

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication("chatmeofficial2003@gmail.com", "gnqx gidp wtlq ffvc")
                }
            })

            val message = MimeMessage(session)
            message.setFrom(InternetAddress("chatmeofficial2003@gmail.com"))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
            message.subject = subject
            message.setText(messageBody)

            Transport.send(message)
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }
}
