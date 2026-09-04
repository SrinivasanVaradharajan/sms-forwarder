package com.smsforwarder.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smsforwarder.app.data.ForwardingRule
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.logic.viewModelProvider

@Composable
fun RulesScreen(
    ruleRepository: RuleRepository,
    onRuleCreated: (ForwardingRule) -> Unit,
    onRuleUpdated: (ForwardingRule) -> Unit,
    onRuleDeleted: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SMS Forwarder Rules") },
                actions = {
                    Button(onClick = { /* Open add rule dialog */ }) {
                        Text("Add")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().scrollState(scrollState)
        ) {
            if (ruleRepository.rules.isEmpty()) {
                Text(
                    text = "No rules configured yet. Tap + to add a rule.",
                    style = MaterialTheme3.typography.bodyMedium
                )
            } else {
                rules {
                    rule ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = rule.name.isNotEmpty() ? rule.name : "Unnamed Rule",
                                        style = MaterialTheme3.typography.titleSmall
                                    )
                                    Text(
                                        text = "Contains: ${rule.containsText}",
                                        style = MaterialTheme3.typography.bodySmall
                                    )
                                    Text(
                                        text = "To: ${rule.targetPhoneNumber}",
                                        style = MaterialTheme3.typography.bodySmall
                                    )
                                }
                                Switch(
                                    checked = rule.isEnabled,
                                    onCheckedChanged = { isEnabled ->
                                        val updatedRule = rule.copy(isEnabled = isEnabled)
                                        onRuleUpdated(updatedRule)
                                    }
                                )
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}