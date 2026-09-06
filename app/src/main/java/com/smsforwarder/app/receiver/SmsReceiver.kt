package com.smsforwarder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import com.smsforwarder.app.SmsForwarderApp
import com.smsforwarder.app.data.ForwardingLog
import com.smsforwarder.app.data.LogRepository
import com.smsforwarder.app.data.RuleRepository

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return

        val app = SmsForwarderApp.instance
        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java) ?: return
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }

        val pdus = intent.extras?.get("pdus") as? Array<ByteArray> ?: return

        for (pdu in pdus) {
            val message = SmsMessage.createFromPdu(pdu)
            val sender = message.originatingAddress ?: continue
            val body = message.messageBody

            forwardSms(sender, body, app.ruleRepository, app.logRepository, smsManager)
        }
    }

    private fun forwardSms(
        sender: String,
        body: String?,
        ruleRepository: RuleRepository,
        logRepository: LogRepository,
        smsManager: SmsManager
    ) {
        if (body == null || body.isEmpty()) return

        val enabledRules = ruleRepository.rules.filter { it.isEnabled }

        for (rule in enabledRules) {
            if (rule.containsText.isEmpty() || body.contains(rule.containsText, ignoreCase = true)) {
                val success = forwardMessage(body, rule.targetPhoneNumber, smsManager)

                logRepository.addLog(
                    ForwardingLog(
                        originalSender = sender,
                        targetPhoneNumber = rule.targetPhoneNumber,
                        matchedRuleName = rule.name,
                        messageBody = body,
                        isSuccess = success,
                        errorMessage = if (success) null else "Send failed"
                    )
                )
                return
            }
        }
    }

    private fun forwardMessage(messageBody: String, targetPhoneNumber: String, smsManager: SmsManager): Boolean {
        return try {
            val parts = smsManager.divideMessage(messageBody)
            if (parts.size == 1) {
                smsManager.sendTextMessage(targetPhoneNumber, null, messageBody, null, null)
            } else {
                smsManager.sendMultipartTextMessage(targetPhoneNumber, null, parts, null, null)
            }
            true
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Failed to forward SMS", e)
            false
        }
    }
}