package com.smsforwarder.app

import android.app.Application
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.data.LogRepository

class SmsForwarderApp : Application() {
    val ruleRepository by lazy { RuleRepository(getSharedPreferences("sms_forwarder_prefs", MODE_PRIVATE)) }
    val logRepository by lazy { LogRepository(getSharedPreferences("sms_forwarder_prefs", MODE_PRIVATE)) }

    companion object {
        lateinit var instance: SmsForwarderApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}