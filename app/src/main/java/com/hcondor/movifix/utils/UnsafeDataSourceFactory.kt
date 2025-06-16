package com.hcondor.movifix.utils

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object UnsafeDataSourceFactory {

    @UnstableApi
    fun create(): DataSource.Factory {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val hostnameVerifier = HostnameVerifier { _, _ -> true }

            val baseFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)

            return object : DataSource.Factory {
                override fun createDataSource(): DataSource {
                    val ds = baseFactory.createDataSource() as DefaultHttpDataSource
                    // Esta línea se elimina porque accede a una propiedad privada:
                    // ds.defaultRequestProperties.set("User-Agent", "ExoPlayer")

                    // ⚠️ Manipulación por reflexión (sólo para pruebas)
                    val field = ds.javaClass.getDeclaredField("connectionFactory")
                    field.isAccessible = true
                    val newFactory = HttpsURLConnectionFactory(sslSocketFactory, hostnameVerifier)
                    field.set(ds, newFactory)
                    return ds
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("No se pudo crear una fuente de datos insegura", e)
        }
    }

    // Clase para forzar el uso de la configuración SSL personalizada
    class HttpsURLConnectionFactory(
        private val sslSocketFactory: SSLSocketFactory,
        private val hostnameVerifier: HostnameVerifier
    ) {
        fun configureConnection(connection: HttpsURLConnection) {
            connection.sslSocketFactory = sslSocketFactory
            connection.hostnameVerifier = hostnameVerifier
        }
    }
}
