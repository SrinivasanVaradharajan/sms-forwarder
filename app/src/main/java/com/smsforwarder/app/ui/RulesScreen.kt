package com.smsforwarder.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smsforwarder.app.data.ForwardingRule
import com.smsforwarder.app.data.RuleRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    ruleRepository: RuleRepository,
    onRuleCreated: (ForwardingRule) -> Unit,
    onRuleUpdated: (ForwardingRule) -> Unit,
    onRuleDeleted: (String) -> Unit
) {
    var rules by remember { mutableStateOf(ruleRepository.rules) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SMS Forwarder Rules") },
                actions = {
                    TextButton(onClick = { showDialog = true }) {
                        Text("Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (rules.isEmpty()) {
                Text(
                    text = "No rules configured yet. Tap Add to create one.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(rules) { rule ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
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
                                            ruleRepository.saveRule(updatedRule)
                                            rules = ruleRepository.rules
                                            onRuleUpdated(updatedRule)
                                        }
                                    )
                                }
                                TextButton(onClick = {
                                    ruleRepository.deleteRule(rule.id)
                                    rules = ruleRepository.rules
                                    onRuleDeleted(rule.id)
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddRuleDialog(
            isVisible = true,
            onDismiss = { showDialog = false },
            onSave = { rule ->
                ruleRepository.saveRule(rule)
                rules = ruleRepository.rules
                onRuleCreated(rule)
                showDialog = false
            }
        )
    }
}