package com.maddog05.madculqui

import android.annotation.SuppressLint
import com.maddog05.madculqui.logic.GenerateTokenRequest
import com.maddog05.madculqui.logic.PayRequest
import com.maddog05.madculqui.network.CulquiService
import okhttp3.OkHttpClient
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

open class MadCulqui(val publicKey: String, val secretKey: String) {

    fun generateTokenRequest(): GenerateTokenRequest {
        return GenerateTokenRequest(this)
    }

    fun payRequest(): PayRequest {
        return PayRequest(this)
    }

    fun getServices(): CulquiService {
        return createRetrofit(URL_CULQUI).create(CulquiService::class.java)
    }

    companion object {

        private const val URL_CULQUI = "https://api.culqi.com/v2/"

        @JvmStatic
        fun with(publicKey: String, secretKey: String): MadCulqui {
            return MadCulqui(publicKey, secretKey)
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