package com.example.quranapp.extensions

import android.content.Context
import com.example.quranapp.model.Surah
import com.example.quranapp.model.SurahInfo
import com.example.quranapp.model.Word
import org.json.JSONObject
import java.io.InputStream

class JsonParser(private val context: Context) {

    fun parseJsonFromRaw(rawResourceId: Int): Map<String, String> {
        val translationMap = mutableMapOf<String, String>()
        try {
            val inputStream: InputStream = context.resources.openRawResource(rawResourceId)
            val jsonStr = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonStr)

            for (key in jsonObject.keys()) {
                val value = jsonObject.getString(key)
                translationMap[key] = value
            }
        } catch (e: Exception) {
        }
        return translationMap
    }

    fun parseWordJson(rawResourceId: Int) : List<Word>? {
        val wordList = mutableListOf<Word>()
        try {
            val inputStream: InputStream = context.resources.openRawResource(rawResourceId)
            val jsonStr = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonStr)

            for(key in jsonObject.keys()) {
                val item = jsonObject.getJSONObject(key)
                val surah = item.getInt("surah")
                val ayah = item.getInt("ayah")
                val position = item.getInt("position")
                val word = Word(surah, ayah, position)
                wordList.add(word)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return wordList
    }

    fun parseSurahJson(rawResourceId: Int): List<Surah>{
        val surahList = mutableListOf<Surah>()

        try {
            val inputStream: InputStream = context.resources.openRawResource(rawResourceId)
            val jsonStr = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonStr)

            for (key in jsonObject.keys()) {
                val item = jsonObject.getJSONObject(key)
                val name = item.getString("name")
                val translation = item.getString("translation")
                val surah = Surah(name, translation)
                surahList.add(surah)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return surahList
    }

    fun parseSurahInfoJson(rawResourceId: Int): List<SurahInfo>{
        val surahInfoList = mutableListOf<SurahInfo>()

        try {
            val inputStream: InputStream = context.resources.openRawResource(rawResourceId)
            val jsonStr = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonStr)

            for(key in jsonObject.keys()){
                val item = jsonObject.getJSONObject(key)
                val name = item.getString("name")
                val nAyah = item.getInt("nAyah")
                val revelationOrder = item.getInt("revelationOrder")
                val type = item.getString("type")
                val start = item.getInt("start")
                val end = item.getInt("end")
                val surahInfo = SurahInfo(name, nAyah, revelationOrder, type, start, end)
                surahInfoList.add(surahInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return surahInfoList
    }
}