package com.maddog05.demomadculqui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.callback.OnPayTransactionListener
import com.maddog05.madculqui.entity.Card

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PUBLIC_KEY = "PUBLIC"
        private const val SECRET_KEY = "SECRET"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generateToken()
    }

    private fun generateToken() {
        debugLog("generateToken")
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
            .generateTokenRequest()
            .setCard(
                Card.Builder()
                    .number("4111111111111111")
                    .expirationMonth(9)
                    .expirationYear(2020)
                    .cvv("123")
                    .email("andree@testing.com")
                    .build()
            )
            .execute(object : OnGenerateTokenListener {
                override fun onSuccess(token: String) {
                    debugLog("generateToken success")
                    payTransaction(token)

                }

                override fun onError(errorMessage: String) {
                    errorLog("generateToken error")
                    errorLog(errorMessage)
                }
            })
    }

    private fun payTransaction(token: String) {
        debugLog("payTransaction with token $token")
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
            .payRequest()
            .setAmount(12.50)
            .setCurrencyCode("PEN")
            .setEmail("andree@testing.com")
            .setSourceId(token)
            .execute(object : OnPayTransactionListener {
                override fun onSuccess(transactionId: String) {
                    debugLog("payTransaction success")
                    debugLog("transactionId is $transactionId")
                }

                override fun onError(errorMessage: String) {
                    errorLog("payTransaction error")
                    errorLog(errorMessage)
                }
            })
    }

    private fun debugLog(text: String) {
        Log.d("#Main", text)
    }

    private fun errorLog(text: String) {
        Log.e("#Main", text)
    }
}
