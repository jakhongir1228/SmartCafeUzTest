package network

import android.content.Context
import android.os.Build
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import java.util.Objects
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.TlsVersion
import okhttp3.internal.http.RealResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.GzipSource
import okio.Okio
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.readystatesoftware.chuck.ChuckInterceptor


class ApiClient {
    private val REQUEST_TIMEOUT: Long = 60
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    private var context: Context? = null
    private var baseUrl: String? = null

    constructor(context: Context?, baseUrl: String?){
        this.context = context
        this.baseUrl = baseUrl
    }

    fun getApiClient(): Retrofit? {
        if (okHttpClient == null) {
            initOkHttp(context)
        }
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    private fun initOkHttp(context: Context?) {
        var trustManagerFactory: TrustManagerFactory? = null
        var sslSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
        var httpClient: OkHttpClient.Builder? = null
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                trustManager = trustManagers[0] as X509TrustManager
                val sslContext = SSLContext.getInstance("TLSv1.2")
                sslContext.init(null, arrayOf<TrustManager?>(trustManager), null)
                sslSocketFactory = sslContext.socketFactory
                httpClient = OkHttpClient().newBuilder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                httpClient.addInterceptor(interceptor)
                httpClient.addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original = chain.request()
                        val requestBuilder =
                            original.newBuilder() // .addHeader("Accept", "application/json")
                                .addHeader(
                                    "Content-Type",
                                    "application/json"
                                ) //                                .addHeader("Accept-Encoding", "gzip")
                        //                                .addHeader("Appa-V", BuildConfig.VERSION_NAME.replace(".", ""))
                        //                                .addHeader("App-Version", BuildConfig.VERSION_NAME.replace(".", ""))
                        return unzip(chain.proceed(requestBuilder.build()))
                    }

                    @Throws(IOException::class)
                    private fun unzip(response: Response): Response {
                        if (response.body() == null) {
                            return response
                        }

                        //check if we have gzip response
                        val contentEncoding = response.headers()["Content-Encoding"]

                        //this is used to decompress gzipped responses
                        return if (contentEncoding != null && contentEncoding == "gzip") {
                            val contentLength = response.body()!!.contentLength()
                            val responseBody = GzipSource(response.body()!!.source())
                            val strippedHeaders = response.headers().newBuilder().build()
                            response.newBuilder().headers(strippedHeaders)
                                .body(
                                    RealResponseBody(
                                        Objects.requireNonNull(
                                            response.body()!!.contentType()
                                        ).toString(), contentLength, Okio.buffer(responseBody)
                                    )
                                )
                                .build()
                        } else {
                            response
                        }
                    }
                })
                 httpClient.addInterceptor(ChuckInterceptor(context));
                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build()
                val specs: MutableList<ConnectionSpec> = ArrayList()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)
                okHttpClient = httpClient
                    .connectionSpecs(specs)
                    .build()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }
        } else {
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val spec1 = ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT)
                .build()
            httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val requestBuilder =
                        original.newBuilder() //                            .addHeader("Accept", "application/json")
                            .addHeader(
                                "Content-Type",
                                "application/json"
                            ) //                            .addHeader("Accept-Encoding", "gzip")
                    //                            .addHeader("Appa-V", BuildConfig.VERSION_NAME.replace(".", ""))
                    //                            .addHeader("App-Version", BuildConfig.VERSION_NAME.replace(".", ""))
                    return chain.proceed(requestBuilder.build())
                    // return unzip(chain.proceed(requestBuilder.build()));
                }

                @Throws(IOException::class)
                private fun unzip(response: Response): Response? {
                    if (response.body() == null) {
                        return response
                    }

                    //check if we have gzip response
                    val contentEncoding = response.headers()["Content-Encoding"]

                    //this is used to decompress gzipped responses
                    return if (contentEncoding != null && contentEncoding == "gzip") {
                        val contentLength = response.body()!!.contentLength()
                        val responseBody = GzipSource(response.body()!!.source())
                        val strippedHeaders = response.headers().newBuilder().build()
                        response.newBuilder().headers(strippedHeaders)
                            .body(
                                RealResponseBody(
                                    Objects.requireNonNull(
                                        response.body()!!.contentType()
                                    ).toString(), contentLength, Okio.buffer(responseBody)
                                )
                            )
                            .build()
                    } else {
                        response
                    }
                }
            })
            httpClient.addInterceptor(ChuckInterceptor(context));
            okHttpClient = httpClient
                .connectionSpecs(Arrays.asList(spec, spec1))
                .build()
        }
    }
}