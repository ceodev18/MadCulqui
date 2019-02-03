package com.maddog05.madculqui

import android.annotation.SuppressLint
import com.google.gson.JsonObject
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.entity.Card
import com.maddog05.madculqui.network.CulquiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.Socket
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

open class MadCulqui(val token: String) {
    var card: Card? = null

    fun generateToken(listener: OnGenerateTokenListener) {
        if (card != null) {
            val body = JsonObject()
            body.addProperty("card_number", card!!.number)
            body.addProperty("expiration_month", card!!.expirationMonth)
            body.addProperty("expiration_year", card!!.expirationYear)
            body.addProperty("cvv", card!!.cvv)
            body.addProperty("email", card!!.email)

            val culqui = getServices()
            culqui.generateToken("Bearer $token", body)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
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

    private fun getServices(): CulquiService {
        return createRetrofit(URL_CULQUI).create(CulquiService::class.java)
    }

    companion object {

        private const val URL_CULQUI = "https://api.culqi.com/v2"

        @JvmStatic
        fun with(token: String): Builder {
            return Builder(token)
        }
    }

    class Builder(val token: String) {
        private var bCard: Card? = null

        fun setCard(card: Card): Builder {
            this.bCard = card
            return this
        }

        fun generateToken(listener: OnGenerateTokenListener) {
            MadCulqui(token).apply {
                card = bCard
            }.generateToken(listener)
        }
    }

    //INIT RETROFIT
    private fun createRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .client(createHttpsClient())
            .build()
    }

    private fun createHttpsClient(): OkHttpClient {
        var builder: OkHttpClient.Builder
        try {
            val sslContext = SSLContext.getInstance("TLSv1.1")
            val trustManager = Array(1) {
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            }
            sslContext.init(null, trustManager, null)
            val socketFactory = CustomTLSSocketFactory()
            builder = OkHttpClient.Builder()
                .sslSocketFactory(socketFactory, object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                })
                .hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
            builder = OkHttpClient.Builder()
        }
        builder.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        return builder.build()
    }

    //CLASSES
    @Suppress("UNUSED_EXPRESSION")
    private class CustomTLSSocketFactory : SSLSocketFactory() {
        private var sslSocketFactory: SSLSocketFactory

        private fun enableTLSOnSocket(socket: Socket?): Socket? {
            if (socket != null && socket is SSLSocket) {
                socket.enabledProtocols = Array(2) { "TLSv1.1"; "TLSv1.2" }
            }
            return socket
        }

        init {
            val context = SSLContext.getInstance("TLS")
            context.init(null, null, null)
            sslSocketFactory = context.socketFactory
        }

        override fun getDefaultCipherSuites(): Array<String> {
            return sslSocketFactory.defaultCipherSuites
        }

        override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket? {
            return enableTLSOnSocket(sslSocketFactory.createSocket(s, host, port, autoClose))
        }

        override fun createSocket(host: String?, port: Int): Socket? {
            return enableTLSOnSocket(sslSocketFactory.createSocket(host, port))
        }

        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket? {
            return enableTLSOnSocket(sslSocketFactory.createSocket(host, port, localHost, localPort))
        }

        override fun createSocket(host: InetAddress?, port: Int): Socket? {
            return enableTLSOnSocket(sslSocketFactory.createSocket(host, port))
        }

        override fun createSocket(
            address: InetAddress?,
            port: Int,
            localAddress: InetAddress?,
            localPort: Int
        ): Socket? {
            return enableTLSOnSocket(sslSocketFactory.createSocket(address, port, localAddress, localPort))
        }

        override fun getSupportedCipherSuites(): Array<String> {
            return sslSocketFactory.supportedCipherSuites
        }

    }
}