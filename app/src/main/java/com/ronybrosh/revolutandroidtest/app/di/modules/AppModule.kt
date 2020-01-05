package com.ronybrosh.revolutandroidtest.app.di.modules

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ronybrosh.revolutandroidtest.app.di.util.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module(includes = [RatesModule::class])
object AppModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrifit(): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            baseUrl("https://revolut.duckdns.org/")
            client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }.build())
        }.build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideResources(application: Application): Resources {
        return application.resources
    }

    @JvmStatic
    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return ViewModelProviderFactory(providers)
    }
}