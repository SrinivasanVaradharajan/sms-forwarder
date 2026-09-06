package com.smsforwarder.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import com.smsforwarder.app.data.LogRepository
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.ui.RulesScreen
import com.smsforwarder.app.ui.theme.SmsForwarderTheme

class MainActivity : ComponentActivity() {

    private lateinit var ruleRepository: RuleRepository
    private lateinit var logRepository: LogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as SmsForwarderApp
        ruleRepository = app.ruleRepository
        logRepository = app.logRepository

        setContent {
            SmsForwarderTheme {
                RulesScreen(
                    ruleRepository = ruleRepository,
                    onRuleCreated = { },
                    onRuleUpdated = { },
                    onRuleDeleted = { }
                )
            }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        val smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        val sendSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)

        if (smsPermission != PackageManager.PERMISSION_GRANTED ||
            sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS),
                1
            )
        }
    }
}