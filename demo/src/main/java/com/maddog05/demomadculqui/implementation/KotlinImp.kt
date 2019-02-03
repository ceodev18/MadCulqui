package com.maddog05.demomadculqui.implementation

import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.entity.Card

class KotlinImp {
    companion object {
        const val TOKEN = ""
    }

    fun generateToken() {
        MadCulqui(TOKEN).apply {
            card = Card().apply {
                number = ""
                expirationMonth = 1
                expirationYear = 2020
                cvv = "123"
                email = "a@a.com"
            }
        }.generateToken(object : OnGenerateTokenListener {
            override fun onSuccess(token: String) {

            }

            override fun onError(errorMessage: String) {

            }
        })
    }

    fun generateTokenWithBuilderPattern() {
        MadCulqui.with(TOKEN)
            .setCard(
                Card.Builder()
                    .number("")
                    .expirationMonth(1)
                    .expirationYear(2020)
                    .cvv("123")
                    .email("a@a.com")
                    .build()
            ).generateToken(object : OnGenerateTokenListener {
                override fun onSuccess(token: String) {

                }

                override fun onError(errorMessage: String) {

                }
            })
    }
}