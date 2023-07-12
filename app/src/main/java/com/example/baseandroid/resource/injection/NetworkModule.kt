package com.example.baseandroid.resource.injection

import android.content.Context
import com.example.baseandroid.BuildConfig
import com.example.baseandroid.data.local.Settings
import com.example.baseandroid.resource.utils.isInternetAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @AuthInterceptorClient
    @Provides
    fun provideAuthClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(networkConnectionInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        return clientBuilder.build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newChain = chain.request().newBuilder()
            .addHeader("accept", "application/json")
        if (Settings.ACCESS_TOKEN.get("").isNotEmpty()) {
            newChain.addHeader("Authorization", "Bearer ${Settings.ACCESS_TOKEN.get("")}")
        }
        return chain.proceed(newChain.build())
    }
}

class NetworkConnectionInterceptor @Inject constructor(@ApplicationContext private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isInternetAvailable()) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}