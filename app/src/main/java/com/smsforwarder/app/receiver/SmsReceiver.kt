package com.smsforwarder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
 android.telephony.SmsManager
import android.util.Log
import com.smsforwarder.app.data.ForwardingLog
import com.smsforwarder.app.data.ForwardingRule
import com.smsforwarder.app.data.LogRepository

class SmsReceiver(ruleRepository: RuleRepository, logRepository: LogRepository, context: Context) : BroadcastReceiver() {

    private val smsManager = context.getSystemService(SmsManager::class.java) ?: throw("SmsManager not available")

    override fun onReceive(context: Context, intent: Intent) {
        val messages = intent.getParcelableArrayExtra("pdus") as? Array<android.telephony.SmsMessage>
            ?: return

        for (message in messages) {
            val sender = message.getOriginatingAddress()
            val body = message.getMessageBody()

            forwardSms(sender, body)
        }
    }

    private fun forwardSms(sender: String, body: String?) {
        if (body == null || body.isEmpty()) return

        val enabledRules = ruleRepository.rules.filter { it.isEnabled }

        var forwarded = false
        var matchedRuleName = ""

        for (rule in enabledRules) {
            if (body.contains(rule.containsText, ignoreCase = true)) {
                matchedRuleName = rule.name
                forwardMessage(body, rule.targetPhoneNumber, sender)
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

    private fun forwardMessage(messageBody: String, targetPhoneNumber: String, sender: String) {
        val message = messageBody

        try {
            val parts = smsManager.divideMessage(message)
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