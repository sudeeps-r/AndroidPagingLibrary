package com.movie.app.core.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.movie.app.BuildConfig
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */


@Module
class HttpModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(server:String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .baseUrl(server)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(@Named("API_KEY")apiKeyInterceptor: Interceptor, @Named("HTTP_LOGGER")httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(httpLoggingInterceptor)
        httpClient.addInterceptor(apiKeyInterceptor)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        return gson
    }

    @Named("API_KEY")
    @Singleton
    @Provides
    fun provideApiKeyIntercepotr(): Interceptor {
        return  object  : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apikey", BuildConfig.API_KEY)
                        .build()

                val requestBuilder = original.newBuilder()
                        .url(url)

                val request = requestBuilder.build()
                return chain.proceed(request)
            }

        }
    }

    @Named("HTTP_LOGGER")
    @Singleton
    @Provides
    fun provideLogger(): HttpLoggingInterceptor {
        val httpLoggingInterceptor= HttpLoggingInterceptor()
        httpLoggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

}