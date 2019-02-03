package com.maddog05.madculqui.callback

interface OnPayTransactionListener {
    fun onSuccess(transactionId: String)
    fun onError(errorMessage: String)
}