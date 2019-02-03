package com.maddog05.demomadculqui.implementation

import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.callback.OnPayTransactionListener
import com.maddog05.madculqui.entity.Card

class KotlinImp {
    companion object {
        const val PUBLIC_KEY = ""
        const val SECRET_KEY = ""
    }

    fun generateToken() {
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
            .generateTokenRequest()
            .setCard(
                Card.Builder()
                    .number("")
                    .expirationMonth(1)
                    .expirationYear(2020)
                    .cvv("123")
                    .email("a@a.com")
                    .build()
            )
            .execute(object : OnGenerateTokenListener {
                override fun onSuccess(token: String) {

                }

                override fun onError(errorMessage: String) {

                }
            })
    }

    fun payTransaction(){
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
            .payRequest()
            .setAmount(12.50)
            .setCurrencyCode("USD")
            .setEmail("a@a.com")
            .setSourceId("myToken")
            .execute(object:OnPayTransactionListener{
                override fun onSuccess(transactionId: String) {

                }

                override fun onError(errorMessage: String) {

                }
            })
    }
}