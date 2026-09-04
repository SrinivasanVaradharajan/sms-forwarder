package com.smsforwarder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val smsIntent = Intent(context, ::class.java)
        smsIntent.setAction("android.intent.action.BOOT_COMPLETED")
        context.startService(Intent(context, ::class.java).setClass(context, SmsForwarderService::class.java))
    }
}