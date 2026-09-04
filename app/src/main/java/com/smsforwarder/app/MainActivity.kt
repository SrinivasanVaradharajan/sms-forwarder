package com.smsforwarder.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import androidx.activity.result.contractActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var ruleRepository: RuleRepository
    private lateinit var logRepository: LogRepository
    private lateinit var smsReceiver: SmsReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SmsForwarderTheme {
            val viewModel = rememberSaveViewModel()

            // Initialize repositories
            val sharedPrefs = getSharedPreferences("sms_forwarder_prefs", Context.MODE_PRIVATE)
            ruleRepository = RuleRepository(sharedPrefs)
            logRepository = LogRepository(sharedPrefs)

            // Register SMS receiver
            smsReceiver = remember { SmsReceiver(ruleRepository, logRepository, this) }
            registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

            // Also register for boot complete
            registerReceiver(smsReceiver, IntentFilter("android.intent.action.BOOT_COMPLETED"))

            // Check permissions
            val smsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS)
            val sendSmsPermission = checkSelfPermission(Manifest.permission.SEND_SMS)

            if (smsPermission != PackageManager.PERMISSION_GRANTED ||
                sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS),
                    1
                )
            }
        }}
    }
}