package com.example.quranapp.model

data class AyahModel(
    val nameSurah: String,
    val ayah: MutableMap<Int, MutableList<LanguageContext>>,
)
