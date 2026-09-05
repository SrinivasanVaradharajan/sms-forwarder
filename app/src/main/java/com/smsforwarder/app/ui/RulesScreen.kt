package com.smsforwarder.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smsforwarder.app.data.ForwardingRule
import com.smsforwarder.app.data.RuleRepository
import com.smsforwarder.app.ui.theme.SmsForwarderTheme

@Composable
fun RulesScreen(
    ruleRepository: RuleRepository,
    onRuleCreated: (ForwardingRule) -> Unit,
    onRuleUpdated: (ForwardingRule) -> Unit,
    onRuleDeleted: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var editingRule by remember { mutableStateOf<ForwardingRule?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SMS Forwarder Rules") },
                actions = {
                    Button(onClick = { showDialog = true }) {
                        Text("Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (ruleRepository.rules.isEmpty()) {
                Text(
                    text = "No rules configured yet. Tap + to add a rule.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(state = scrollState) {
                    items(ruleRepository.rules) { rule ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = if (rule.name.isNotEmpty()) rule.name else "Unnamed Rule",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Text(
                                            text = "Contains: ${rule.containsText}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "To: ${rule.targetPhoneNumber}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Switch(
                                        checked = rule.isEnabled,
                                        onCheckedChange = { isEnabled ->
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
}