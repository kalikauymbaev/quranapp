package com.example.quranapp

import android.app.Application
import com.example.quranapp.di.getRepositoriesModule
import com.example.quranapp.di.getViewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class QuranApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    getViewModelModules(),
                    getRepositoriesModule(),
                )
            )
        }
    }
}
