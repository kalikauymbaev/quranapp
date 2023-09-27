package com.example.quranapp.di

import com.example.quranapp.mvvm.MainViewModel
import org.koin.dsl.module


fun getViewModelModules() = module {
    single { MainViewModel(get()) }
}
