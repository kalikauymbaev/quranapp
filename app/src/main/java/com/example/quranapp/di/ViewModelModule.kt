package com.example.quranapp.di

import com.example.quranapp.MainViewModel
import org.koin.dsl.module


fun getViewModelModules() = module {
    single { MainViewModel(get()) }
}
