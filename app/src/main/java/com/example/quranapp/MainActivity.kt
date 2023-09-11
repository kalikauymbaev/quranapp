package com.example.quranapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quranapp.model.LanguageContext
import com.example.quranapp.model.Word
import com.example.quranapp.ui.theme.QuranAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val finalList = mutableListOf<LanguageContext>()
    private val sections = mutableListOf<Word>()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseJson()
    }

    private fun parseJson() {
        val jsonParser = JsonParser(this)

        val arabicResourceId = R.raw.arab_full_text
        val russianResourceId = R.raw.en_full_text
        val surahNameResourceId = R.raw.en_surah
        val wordResourceId = R.raw.word

        val arabicMap = jsonParser.parseJsonFromRaw(arabicResourceId)
        val russianMap = jsonParser.parseJsonFromRaw(russianResourceId)
        val surahNameList = jsonParser.parseSurahJson(surahNameResourceId)
        val wordList = jsonParser.parseWordJson(wordResourceId)!!

        for ((arabicKey, arabicValue) in arabicMap) {
            val russianTranslation = russianMap[arabicKey]

            finalList.add(LanguageContext(arabicValue, russianTranslation ?: ""))
        }

        setContent {
            QuranAppTheme {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),) {
                    itemsIndexed(finalList) { index, content ->
                        Row {
                            Text(text = "${surahNameList[wordList[index].surah-1].name} - ${surahNameList[wordList[index].surah-1].translation}",
                                modifier = Modifier.fillMaxWidth())
                        }
                        Column(
                            modifier = Modifier
                                .background(
                                    colorResource(id = R.color.lightGray),
                                    shape = RoundedCornerShape(20)
                                )
                                .padding(16.dp)
                        ) {
                            Text(text = content.arabicLang)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = content.russianLang)
                        }
                    }
                }
            }
        }
    }
}