package com.smsforwarder.app.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RuleRepository(private val sharedPrefs: SharedPreferences) {
    private val gson = Gson()
    private val rulesKey = "forwarding_rules"

    val rules: List<ForwardingRule>
        get() {
            val json = sharedPrefs.getString(rulesKey, "[]")
            val typeRef: Type = object : TypeToken<List<ForwardingRule>>() {}.type
            return gson.fromJson(json, typeRef) ?: emptyList()
        }

    fun saveRule(rule: ForwardingRule) {
        val currentRules = rules
        val updatedRules = if (currentRules.any { it.id == rule.id }) {
            currentRules.map { if (it.id == rule.id) rule else it }
        } else {
            currentRules + rule
        }
        sharedPrefs.edit().putString(
            rulesKey,
            gson.toJson(updatedRules)
        ).apply()
    }

    fun deleteRule(id: String) {
        val currentRules = rules
        val updatedRules = currentRules.filter { it.id != id }
        sharedPrefs.edit().putString(
            rulesKey,
            gson.toJson(updatedRules)
        ).apply()
    }
}

class LogRepository(private val sharedPrefs: SharedPreferences) {
    private val gson = Gson()
    private val logsKey = "forwarding_logs"
    private val maxLogs = 500

    val logs: List<ForwardingLog>
        get() {
            val json = sharedPrefs.getString(logsKey, "[]")
            val typeRef: Type = object : TypeToken<List<ForwardingLog>>() {}.type
            return gson.fromJson(json, typeRef) ?: emptyList()
        }

    fun addLog(log: ForwardingLog) {
        val currentLogs = logs
        val updatedLogs = (currentLogs + log)
            .sortedBy { -it.timestamp }
            .take(maxLogs)
        sharedPrefs.edit().putString(
            logsKey,
            gson.toJson(updatedLogs)
        ).apply()
    }

    fun clearLogs() {
        sharedPrefs.edit().putString(logsKey, "[]").apply()
    }
}