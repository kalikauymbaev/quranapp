package com.example.quranapp.di

import com.example.quranapp.repositories.ListofQuranRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun getRepositoriesModule() = module {
    single { ListofQuranRepository(context = androidContext()) }
}
