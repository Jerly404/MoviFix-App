package com.hcondor.movifix.utils

import java.security.cert.X509Certificate
import javax.net.ssl.*

object InsecureSSLSocketFactory {
    val trustManager: X509TrustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    val sslContext: SSLContext = SSLContext.getInstance("SSL").apply {
        init(null, arrayOf<TrustManager>(trustManager), java.security.SecureRandom())
    }
}