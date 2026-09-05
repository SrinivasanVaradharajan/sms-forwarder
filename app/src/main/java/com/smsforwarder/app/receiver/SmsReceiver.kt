package com.smsforwarder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.smsforwarder.app.SmsForwarderApp
import com.smsforwarder.app.data.ForwardingLog
import com.smsforwarder.app.data.ForwardingRule

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val messages = intent.getParcelableArrayExtra("pdus") as? Array<android.telephony.SmsMessage>
            ?: return

        val app = SmsForwarderApp.instance
        val smsManager = context.getSystemService(SmsManager::class.java) ?: return

        for (message in messages) {
            val sender = message.getOriginatingAddress()
            val body = message.getMessageBody()

            forwardSms(sender, body, app.ruleRepository, app.logRepository, smsManager)
        }
    }

    private fun forwardSms(sender: String, body: String?, ruleRepository: com.smsforwarder.app.data.RuleRepository, logRepository: com.smsforwarder.app.data.LogRepository, smsManager: SmsManager) {
        if (body == null || body.isEmpty()) return

        val enabledRules = ruleRepository.rules.filter { it.isEnabled }

        var forwarded = false
        var matchedRuleName = ""

        for (rule in enabledRules) {
            if (body.contains(rule.containsText, ignoreCase = true)) {
                matchedRuleName = rule.name
                forwardMessage(body, rule.targetPhoneNumber, sender, smsManager)
                forwarded = true
                break
            }
        }

        if (forwarded) {
            val log = ForwardingLog(
                originalSender = sender,
                targetPhoneNumber = matchedRuleName,
                matchedRuleName = matchedRuleName,
                messageBody = body,
                isSuccess = true
            )
            logRepository.addLog(log)
        }
    }

    private fun forwardMessage(messageBody: String, targetPhoneNumber: String, sender: String, smsManager: SmsManager) {
        try {
            val parts = smsManager.divideMessage(messageBody)
            for (i in parts.indices) {
                smsManager.sendMultipartTextMessage(
                    targetPhoneNumber,
                    null,
                    parts[i],
                    null,
                    null
                )
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Failed to forward SMS", e)
        }
    }
}