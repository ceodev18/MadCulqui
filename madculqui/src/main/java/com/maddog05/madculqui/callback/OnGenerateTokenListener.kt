package com.maddog05.madculqui.callback

interface OnGenerateTokenListener {
    fun onSuccess(token: String)
    fun onError(errorMessage: String)
}