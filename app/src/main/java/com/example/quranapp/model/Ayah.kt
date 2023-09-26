package com.example.quranapp.model

data class Ayah(
    var surah: String,
    var ayahs: MutableList<LanguageContext>,
)
