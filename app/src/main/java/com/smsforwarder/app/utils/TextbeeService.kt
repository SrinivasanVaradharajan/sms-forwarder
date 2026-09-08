package com.smsforwarder.app.utils

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TextbeeApi {
    @POST("api/v1/send")
    suspend fun sendMessage(
        @Header("Authorization") authHeader: String,
        @Query("to") to: String,
        @Query("body") body: String,
        @Query("from") from: String = "SMS_FORWARDER"
    ): MessageResponse

    @GET("api/v1/status")
    suspend fun getMessageStatus(
        @Header("Authorization") authHeader: String,
        @Query("id") messageId: String
    ): MessageStatus
}

// Simplified response classes for Retrofit
class MessageResponse(
    val success: Boolean,
    val messageId: String,
    val status: String,
    val error: String? = null
)

class MessageStatus(
    val id: String,
    val status: String,
    val timestamp: Long,
    val to: String,
    val from: String,
    val body: String
)

object TextbeeService {
    private var api: TextbeeApi? = null
    private var authToken: String? = null

    fun initialize(context: Context, apiKey: String) {
        authToken = apiKey
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.textbee.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(TextbeeApi::class.java)
    }

    suspend fun sendSms(to: String, body: String): Result<String> {
        return try {
            if (api == null || authToken == null) {
                return Result.failure(Exception("Textbee API not initialized"))
            }

            val response = api!!.sendMessage("Bearer $authToken", to, body)
            if (response.success) {
                Result.success(response.messageId)
            } else {
                Result.failure(Exception(response.error ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessageStatus(messageId: String): Result<MessageStatus> {
        return try {
            if (api == null || authToken == null) {
                return Result.failure(Exception("Textbee API not initialized"))
            }

            val response = api!!.getMessageStatus("Bearer $authToken", messageId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
