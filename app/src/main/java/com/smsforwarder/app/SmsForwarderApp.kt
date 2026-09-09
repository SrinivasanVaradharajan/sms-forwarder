package com.smsforwarder.app

import android.app.Application
import android.content.SharedPreferences
import com.smsforwarder.app.data.LogRepository
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.utils.TextbeeService

class SmsForwarderApp : Application() {
    private var _apiKey: String = "txb_GqpA3xNIDInaWQGye0DFlgPS1bVMW6sP"
    private var _sharedPrefs: SharedPreferences? = null

    val ruleRepository: RuleRepository
        get() = RuleRepository(_sharedPrefs!!)

    val logRepository: LogRepository
        get() = LogRepository(_sharedPrefs!!)

    fun setApiKey(apiKey: String) {
        _apiKey = apiKey
        initializeApiService()
    }

    fun getApiKey(): String = _apiKey

    private fun initializeApiService() {
        TextbeeService.initialize(this, _apiKey)
    }

    companion object {
        lateinit var instance: SmsForwarderApp
            private set

        fun getInstance(): SmsForwarderApp = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        _sharedPrefs = getSharedPreferences("sms_forwarder_prefs", android.content.Context.MODE_PRIVATE)
    }
}