package com.maddog05.madculqui.logic

import com.google.gson.JsonObject
import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.entity.Card
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenerateTokenRequest(private val madCulqui: MadCulqui) {
    private var card: Card? = null

    fun setCard(card: Card): GenerateTokenRequest {
        this.card = card
        return this
    }

    fun execute(listener: OnGenerateTokenListener) {
        if (card != null) {
            val body = JsonObject()
            body.addProperty("card_number", card!!.number)
            body.addProperty("expiration_month", card!!.expirationMonth)
            body.addProperty("expiration_year", card!!.expirationYear)
            body.addProperty("cvv", card!!.cvv)
            body.addProperty("email", card!!.email)

            madCulqui.getServices().generateToken("Bearer ${madCulqui.publicKey}", body)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.code() == 201 && response.body() != null) {
                            val elementId = response.body()!!.get("id")
                            if (elementId != null && elementId.isJsonPrimitive)
                                listener.onSuccess(elementId.asString)
                            else
                                listener.onError("Error in get token from response")
                        } else
                            listener.onError("Service response with errorCode ${response.code()}")
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        listener.onError("Error with services")
                    }
                })
        } else
            listener.onError("Card is empty")
    }
}