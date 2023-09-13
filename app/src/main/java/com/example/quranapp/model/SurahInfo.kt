package com.example.quranapp.model

data class SurahInfo(
    val name: String,
    val nAyah: Int,
    val revelationOrder: Int,
    val type: String,
    val start: Int,
    val end: Int,
)
