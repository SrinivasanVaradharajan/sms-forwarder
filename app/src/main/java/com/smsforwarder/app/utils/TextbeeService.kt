package com.smsforwarder.app.utils

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class SendSmsRequest(
    val recipients: List<String>,
    val message: String
)

data class SendSmsResponse(
    val success: Boolean,
    val error: String? = null
)

interface TextbeeApi {
    @POST("api/v1/gateway/send-sms")
    suspend fun sendSms(
        @Header("x-api-key") apiKey: String,
        @Body request: SendSmsRequest
    ): SendSmsResponse
}

object TextbeeService {
    private var api: TextbeeApi? = null
    private var apiKey: String? = null

    fun initialize(context: Context, key: String) {
        apiKey = key
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.textbee.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(TextbeeApi::class.java)
    }

    suspend fun sendSms(to: String, body: String): Result<String> {
        return try {
            val currentApi = api
            val currentKey = apiKey
            if (currentApi == null || currentKey.isNullOrEmpty()) {
                return Result.failure(Exception("Textbee API not initialized"))
            }

            val request = SendSmsRequest(
                recipients = listOf(to),
                message = body
            )

            val response = currentApi.sendSms(currentKey, request)
            if (response.success) {
                Result.success("Message sent successfully")
            } else {
                Result.failure(Exception(response.error ?: "Failed to send SMS via Textbee"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}