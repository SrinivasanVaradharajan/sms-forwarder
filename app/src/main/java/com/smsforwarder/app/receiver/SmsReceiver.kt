package com.smsforwarder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import com.smsforwarder.app.SmsForwarderApp
import com.smsforwarder.app.data.ForwardingLog
import com.smsforwarder.app.data.LogRepository
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.utils.TextbeeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return

        val app = SmsForwarderApp.instance

        val pdus = intent.extras?.get("pdus") as? Array<ByteArray> ?: return

        for (pdu in pdus) {
            val message = SmsMessage.createFromPdu(pdu)
            val sender = message.originatingAddress ?: continue
            val body = message.messageBody

            forwardSms(sender, body, app.ruleRepository, app.logRepository)
        }
    }

    private fun forwardSms(
        sender: String,
        body: String?,
        ruleRepository: RuleRepository,
        logRepository: LogRepository
    ) {
        if (body == null || body.isEmpty()) return

        val enabledRules = ruleRepository.rules.filter { it.isEnabled }

        for (rule in enabledRules) {
            if (rule.containsText.isEmpty() || body.contains(rule.containsText, ignoreCase = true)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val success = sendMessageWithTextbee(rule.targetPhoneNumber, body)

                    logRepository.addLog(
                        ForwardingLog(
                            originalSender = sender,
                            targetPhoneNumber = rule.targetPhoneNumber,
                            matchedRuleName = rule.name,
                            messageBody = body,
                            isSuccess = success,
                            errorMessage = if (success) null else "Failed to send via Textbee"
                        )
                    )
                }
                return
            }
        }
    }

    private suspend fun sendMessageWithTextbee(targetPhoneNumber: String, messageBody: String): Boolean {
        return try {
            val result = TextbeeService.sendSms(targetPhoneNumber, messageBody)
            result.isSuccess
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Failed to forward SMS via Textbee", e)
            false
        }
    }
}