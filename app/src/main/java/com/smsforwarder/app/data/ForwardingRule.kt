package com.smsforwarder.app.data

data class ForwardingRule(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val containsText: String = "",
    val targetPhoneNumber: String = "",
    var isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)