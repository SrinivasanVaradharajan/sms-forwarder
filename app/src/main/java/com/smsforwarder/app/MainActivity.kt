package com.smsforwarder.app

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.smsforwarder.app.ui.RulesScreen
import com.smsforwarder.app.ui.AddRuleDialog
import com.smsforwarder.app.data.ForwardingRule
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
                var showDialog by remember { mutableStateOf(false) }
                var selectedRule by remember { mutableStateOf<ForwardingRule?>(null) }

                RulesScreen(
                    ruleRepository = ruleRepository,
                    onRuleCreated = { rule -> ruleRepository.saveRule(rule) },
                    onRuleUpdated = { rule -> ruleRepository.saveRule(rule) },
                    onRuleDeleted = { id -> ruleRepository.deleteRule(id) }
                )

                if (showDialog) {
                    AddRuleDialog(
                        isVisible = showDialog,
                        onDismiss = { showDialog = false },
                        rule = selectedRule,
                        onSave = { rule ->
                            ruleRepository.saveRule(rule)
                            showDialog = false
                        }
                    )
                }
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