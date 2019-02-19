package com.maddog05.madculqui.logic

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnPayTransactionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayRequest(private val madCulqui: MadCulqui) {
    companion object {
        const val CURRENCY_PEN = "PEN"
        const val CURRENCY_USD = "USD"
    }

    //min 100 max 999900 example 100.00 must be 10000
    private var amount = 0
    private var currencyCode = ""
    private var email = ""
    private var sourceId = ""

    fun setAmount(amount: Int): PayRequest {
        this.amount = amount
        return this
    }

    fun setAmount(amount: Double): PayRequest {
        this.amount = (amount * 100).toInt()
        return this
    }

    fun setCurrencyCode(currencyCode: String): PayRequest {
        this.currencyCode = currencyCode.toUpperCase()
        return this
    }

    fun setEmail(email: String): PayRequest {
        this.email = email
        return this
    }

    fun setSourceId(sourceId: String): PayRequest {
        this.sourceId = sourceId
        return this
    }

    fun execute(listener: OnPayTransactionListener) {
        if (amount < 100 || amount > 999900)
            listener.onError("Amount out of range")
        else if (currencyCode.isEmpty())
            listener.onError("Currency code is required")
        else if (sourceId.isEmpty())
            listener.onError("Token is required")
        else {
            val body = JsonObject()
            body.addProperty("amount", amount)
            body.addProperty("currency_code", currencyCode)
            body.addProperty("email", email)
            body.addProperty("source_id", sourceId)
            madCulqui.getServices().payTransaction("Bearer ${madCulqui.secretKey}", body)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.code() == 201 && response.body() != null) {
                            val elementId = response.body()!!.get("id")
                            if (elementId != null && elementId.isJsonPrimitive)
                                listener.onSuccess(elementId.asString)
                            else
                                listener.onError("Error in get transactionId from response")
                        } else {
                            val errorBody = JsonParser().parse(response.errorBody()!!.string()).asJsonObject
                            val errorType = errorBody.get("type").asString
                            val errorMessage = errorBody.get("merchant_message").asString
                            listener.onError("${response.code()}: $errorType: $errorMessage")
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        listener.onError("Error with services")
                    }
                })
        }
    }
}