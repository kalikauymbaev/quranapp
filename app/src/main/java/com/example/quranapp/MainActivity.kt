package com.example.quranapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranapp.model.LanguageContext
import com.example.quranapp.model.Surah
import com.example.quranapp.ui.theme.QuranAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val finalList = mutableListOf<LanguageContext>()
    private val surahLineMap = mutableMapOf<Int, List<LanguageContext>>()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        parseJson()
    }

    private fun parseJson() {
        val jsonParser = JsonParser(this)

        val arabicResourceId = R.raw.arab_full_text
        val russianResourceId = R.raw.en_full_text
        val surahNameResourceId = R.raw.en_surah
        val wordResourceId = R.raw.word
        val surahInfo = R.raw.surah

        val arabicMap = jsonParser.parseJsonFromRaw(arabicResourceId)
        val russianMap = jsonParser.parseJsonFromRaw(russianResourceId)
        val surahNameList = jsonParser.parseSurahJson(surahNameResourceId)
        val wordList = jsonParser.parseWordJson(wordResourceId)!!
        val surahInfoList = jsonParser.parseSurahInfoJson(surahInfo)

        for ((arabicKey, arabicValue) in arabicMap) {
            val russianTranslation = russianMap[arabicKey]

            finalList.add(LanguageContext(arabicValue, russianTranslation ?: ""))
        }

        for (i in surahInfoList.indices) {
            val newList = mutableListOf<LanguageContext>()
            for (k in wordList.indices) {
                if (wordList[k].surah == i + 1) {
                    finalList[k].let { newList.add(it) }
                }
            }
            surahLineMap[i] = newList
        }

        setContent {
            QuranAppTheme {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(surahNameList) { index, content ->
                        QuranList(
                            index = index,
                            surahNameList = surahNameList,
                            ayahLineMap = surahLineMap,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuranList(
    index: Int,
    surahNameList: List<Surah>,
    ayahLineMap: Map<Int, List<LanguageContext>>,
) {
    Row {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${surahNameList[index].name} - ${surahNameList[index].translation}",
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
    }

    FlowRow(
        maxItemsInEachRow = 5,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ayahLineMap[index]!!.forEach { item ->
            Column(
                modifier = Modifier
                    .background(
                        colorResource(id = R.color.lightGray),
                        shape = RoundedCornerShape(20)
                    )
                    .padding(10.dp),
                horizontalAlignment = Alignment.End,

                ) {
                Text(text = item.arabicLang, textAlign = TextAlign.End)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = item.russianLang, textAlign = TextAlign.End)
            }
        }
    }

//    LazyHorizontalStaggeredGrid(
//        modifier = Modifier.height(176.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        rows = StaggeredGridCells.Adaptive(50.dp)
//    ) {
//        items(ayahLineMap[index]!!) { item->
//            Column(
//                modifier = Modifier
//                    .background(
//                        colorResource(id = R.color.lightGray),
//                        shape = RoundedCornerShape(20)
//                    )
//                    .padding(16.dp)
//            ) {
//                Text(text = item.arabicLang)
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(text = item.russianLang)
//            }
//        }
//        surahInfoList.forEachIndexed { ind, languageContext ->
//            if (ind in surahInfoList[index].start..surahInfoList[index].end) {
//                Timber.tag("getInfo").i(ind.toString())
//                Timber.tag("getInfo").i("true")
//            } else{
//                Timber.tag("getInfo").i("true")
//            }
//        }
//        items(finalList) { content ->
//            Column(
//                modifier = Modifier
//                    .background(
//                        colorResource(id = R.color.lightGray),
//                        shape = RoundedCornerShape(20)
//                    )
//                    .padding(16.dp)
//            ) {
//                Text(text = content.arabicLang)
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(text = content.russianLang)
//            }
//        }
}

//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 80.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//        itemsIndexed(finalList) { _, content ->
//            Column(
//                modifier = Modifier
//                    .background(
//                        colorResource(id = R.color.lightGray),
//                        shape = RoundedCornerShape(20)
//                    )
//                    .padding(16.dp)
//            ) {
//                Text(text = content.arabicLang)
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(text = content.russianLang)
//            }
//        }
//    }
//}