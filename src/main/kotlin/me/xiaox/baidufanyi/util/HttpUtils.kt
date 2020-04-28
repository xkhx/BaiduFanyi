package me.xiaox.baidufanyi.util

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object HttpUtils {
    private const val CONNECT_TIME_OUT = 5000
    private const val READ_TIME_OUT = 5000

    fun get(url: String, mapParam: Map<String, String>?): Map<String, String> {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        }), null)

        val urlParam = if (mapParam == null) url else "${url}?${converMap2String(mapParam)}"
        val connection = buildURLConnection(urlParam)
        if (connection is HttpsURLConnection) {
            connection.sslSocketFactory = sslContext.socketFactory
        }
        connection.setContentType()
        connection.connect()
        val code = connection.responseCode
        val inStream = (if (code == 200) connection.inputStream else connection.errorStream)
            ?: return mapOf("code" to code.toString(), "result" to "")
        val result = inStream.bufferedReader().lineSequence().joinToString()
        inStream.close()
        connection.disconnect()
        return mapOf("code" to code.toString(), "result" to result)
    }

    private fun buildURLConnection(urlLink: String): HttpURLConnection {
        val url = URL(urlLink)
        val connection = url.openConnection() as HttpURLConnection
        connection.let {
            it.requestMethod = "GET"
            it.connectTimeout = CONNECT_TIME_OUT
            it.readTimeout = READ_TIME_OUT
            it.doInput = true
            it.doOutput = true
            it.useCaches = false
            it.instanceFollowRedirects = true
        }
        return connection
    }

    private fun URLConnection.setContentType(type: Int = 0) {
        val typeString = when (type) {
            1 -> "application/x-www-form-urlencoded"
            2 -> "application/x-java-serialized-object"
            else -> "application/json;charset=UTF-8"
        }
        this.setRequestProperty("Content-Type", typeString)
    }

    private fun converMap2String(param: Map<String, String>, isEncode: Boolean = true): String {
        return param.keys.joinToString(separator = "&") { key ->
            val value = if (isEncode) URLEncoder.encode(param[key], "UTF-8") else param[key]
            "$key=$value"
        }
    }
}