package com.example.quranapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quranapp.model.Ayah
import com.example.quranapp.model.AyahModel
import com.example.quranapp.model.LanguageContext
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val finalList = mutableListOf<LanguageContext>()
    private val ayahList = mutableListOf<Ayah>()
    private val surahLineList = mutableListOf<AyahModel>()
    val finalLiveData: MutableLiveData<List<AyahModel>> = MutableLiveData()

    fun getFinalList(){
        viewModelScope.launch {
            val jsonParser = JsonParser(application)

            val arabicResourceId = R.raw.arab_full_text
            val russianResourceId = R.raw.en_full_text
            val surahNameResourceId = R.raw.en_surah
            val wordResourceId = R.raw.word
            val surahInfo = R.raw.surah

            val arabicMap = jsonParser.parseJsonFromRaw(arabicResourceId)
            val russianMap = jsonParser.parseJsonFromRaw(russianResourceId)
//            val surahNameList = jsonParser.parseSurahJson(surahNameResourceId)
            val wordList = jsonParser.parseWordJson(wordResourceId)!!
            val surahInfoList = jsonParser.parseSurahInfoJson(surahInfo)

            for ((arabicKey, arabicValue) in arabicMap) {
                val russianTranslation = russianMap[arabicKey]
                finalList.add(LanguageContext(arabicValue, russianTranslation ?: "", 0))
            }

            for (i in 1..114) {
                val newList = mutableListOf<LanguageContext>()
                for (k in wordList.indices) {
                    if (wordList[k].surah == i) {
                        finalList[k].ayah = wordList[k].ayah
                        finalList[k].let { newList.add(it) }
                    }
                }
                val ayah = Ayah(
                    surah = surahInfoList[i - 1].name,
                    ayahs = newList
                )
                ayahList.add(ayah)
            }

            for (i in ayahList.indices) {
                val newMap = mutableMapOf<Int, MutableList<LanguageContext>>()
                for (j in 1..surahInfoList[i].nAyah) {
                    val newList = mutableListOf<LanguageContext>()
                    for (k in ayahList[i].ayahs.indices) {
                        if (ayahList[i].ayahs[k].ayah == j) {
                            newList.add(ayahList[i].ayahs[k])
                        }
                    }
                    newMap[j] = newList
                }

                val ayahModel = AyahModel(ayahList[i].surah, newMap)
                surahLineList.add(ayahModel)
            }

            finalLiveData.postValue(surahLineList)
        }
    }
}