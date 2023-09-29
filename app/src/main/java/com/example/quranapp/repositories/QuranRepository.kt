package com.example.quranapp.repositories

import com.example.quranapp.model.AyahModel

interface QuranRepository {
    suspend fun getListOfFullQuran(): List<AyahModel>
}
