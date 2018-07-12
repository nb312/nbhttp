package com.nb.nbhttp.inter

import com.nb.nbhttp.inter.Timeout.Companion.TIMEOUT_LONG
import com.nb.nbhttp.inter.Timeout.Companion.TIMEOUT_NORMAL
import com.nb.nbhttp.inter.Timeout.Companion.TIMEOUT_SHORT
import com.nb.nbhttp.param.NBaseParam
import com.nb.nbhttp.param.NTimeoutType
import okhttp3.OkHttpClient
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by NieBin on 2018-07-09
 * Github: https://github.com/nb312
 * Email: niebin312@gmail.com
 */


/**
 * SSL 网络相关
 */
private const val SSL = "SSL"

interface IHttpClient {
    fun createHttpClient(): OkHttpClient?
}

/**this is for timeout params*/
class Timeout() {
    companion object {
        /**this is short time out with 2 seconds.*/
        const val TIMEOUT_SHORT = 3_000L

        /**this is normal time out with 6 seconds.*/
        const val TIMEOUT_NORMAL = 6_000L

        /**this is long time out with 20 seconds.*/
        const val TIMEOUT_LONG = 20_000L
    }

    constructor(time: Long) : this() {
        connectTimeout = time
        writeTimeout = time
        readTimeout = time
    }

    var connectTimeout: Long = TIMEOUT_NORMAL
    var writeTimeout: Long = TIMEOUT_NORMAL
    var readTimeout: Long = TIMEOUT_NORMAL

}

class NBHttpClient(var paramN: NBaseParam) : IHttpClient {

    override fun createHttpClient(): OkHttpClient? {
        var okHttpClient: OkHttpClient? = null
        val trustManager = arrayOf<TrustManager>(TrustSSL())
        try {
            val sslContext = SSLContext.getInstance(SSL)
            sslContext.init(null, trustManager, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            var timeout = when (paramN.timeoutType) {
                NTimeoutType.SHORT -> Timeout(TIMEOUT_SHORT)
                NTimeoutType.NORMAL -> Timeout(TIMEOUT_NORMAL)
                NTimeoutType.LONG -> Timeout(TIMEOUT_LONG)
                NTimeoutType.SELF_DEFINE -> paramN.timeout
            }
            okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(timeout.connectTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout.connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout.connectTimeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(getInterceptor())
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier { _, _ -> true }
                    .build()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        return okHttpClient
    }

    private fun getInterceptor(): IBaseInterceptor {
        return BaseInterceptor(paramN)
    }
}


class TrustSSL : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }
}

