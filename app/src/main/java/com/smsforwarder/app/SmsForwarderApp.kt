package com.smsforwarder.app

import android.app.Application
import com.smsforwarder.app.utils.TextbeeService

class SmsForwarderApp : Application() {
    private var _apiKey: String = "txb_GqpA3xNIDInaWQGye0DFlgPS1bVMW6sP"

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
        initializeApiService()
    }
}