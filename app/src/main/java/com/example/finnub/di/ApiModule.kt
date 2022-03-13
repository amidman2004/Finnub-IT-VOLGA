package com.example.finnub.di

import com.example.finnub.data.api.ApiRepositoryImpl
import com.example.finnub.data.api.ApiRequests
import com.example.finnub.data.api.ApiURLs.BASE_URL
import com.example.finnub.domain.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideApiRepository(apiRequests: ApiRequests):ApiRepository{
        return ApiRepositoryImpl(api = apiRequests)
    }

    @Provides
    @Singleton
    fun provideApiRequests(client: OkHttpClient):ApiRequests{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor{interceptor->
                val requets = interceptor.request()
                    .newBuilder()
                    .header("X-Finnhub-Token","c8krd0aad3ibbdm42ma0")
                    .build()
                interceptor.proceed(requets)
            }
            .build()
    }
}