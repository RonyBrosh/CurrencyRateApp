package com.ronybrosh.currencyrateapp.app.di.modules

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ronybrosh.currencyrateapp.app.di.util.ViewModelKey
import com.ronybrosh.currencyrateapp.data.api.RatesApi
import com.ronybrosh.currencyrateapp.data.repositories.RatesRepositoryImpl
import com.ronybrosh.currencyrateapp.domain.repositories.RatesRepository
import com.ronybrosh.currencyrateapp.presentation.features.rates.view.RatesListFragment
import com.ronybrosh.currencyrateapp.presentation.features.rates.view.RatesViewModel
import com.ronybrosh.currencyrateapp.presentation.util.UIRateUtil
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class RatesModule {
    @ContributesAndroidInjector(modules = [RatesDependenciesModule::class])
    abstract fun contributeRatesListFragment(): RatesListFragment

    @Module
    object RatesDependenciesModule {
        @JvmStatic
        @Provides
        fun provideRatesApi(retrofit: Retrofit): RatesApi {
            return retrofit.create(RatesApi::class.java)
        }

        @JvmStatic
        @Provides
        fun provideRatesRepository(ratesApi: RatesApi): RatesRepository {
            return RatesRepositoryImpl(ratesApi)
        }

        @JvmStatic
        @Provides
        fun provideUIRateUtil(resources: Resources): UIRateUtil {
            return UIRateUtil(resources)
        }

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(RatesViewModel::class)
        fun provideRatesViewModel(
            ratesRepository: RatesRepository,
            uiRateUtil: UIRateUtil
        ): ViewModel {
            return RatesViewModel(ratesRepository, uiRateUtil)
        }

        @JvmStatic
        @Provides
        fun injectRatesViewModel(
            factory: ViewModelProvider.Factory,
            target: RatesListFragment
        ): RatesViewModel {
            return ViewModelProviders.of(target, factory).get(RatesViewModel::class.java)
        }
    }
}