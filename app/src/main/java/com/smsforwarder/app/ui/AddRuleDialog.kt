package com.smsforwarder.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smsforwarder.app.data.ForwardingRule

@Composable
fun AddRuleDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    rule: ForwardingRule? = null,
    onSave: (ForwardingRule) -> Unit
) {
    var name by remember { mutableStateOf(rule?.name ?: "") }
    var containsText by remember { mutableStateOf(rule?.containsText ?: "") }
    var targetPhoneNumber by remember { mutableStateOf(rule?.targetPhoneNumber ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Rule Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = containsText,
                onValueChange = { containsText = it },
                label = { Text("Contains Text") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = targetPhoneNumber,
                onValueChange = { targetPhoneNumber = it },
                label = { Text("Target Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onSave(
                            ForwardingRule(
                                name = name,
                                containsText = containsText,
                                targetPhoneNumber = targetPhoneNumber
                            )
                        )
                        onDismiss()
                    }
                ) {
                    Text("Save Rule")
                }

                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}