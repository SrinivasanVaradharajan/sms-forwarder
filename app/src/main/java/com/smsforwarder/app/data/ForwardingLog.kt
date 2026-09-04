package com.smsforwarder.app.data

data class ForwardingLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val originalSender: String = "",
    val targetPhoneNumber: String = "",
    val matchedRuleName: String = "",
    val messageBody: String = "",
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)