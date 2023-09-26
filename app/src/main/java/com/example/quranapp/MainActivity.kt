package com.example.quranapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranapp.model.Ayah
import com.example.quranapp.model.AyahModel
import com.example.quranapp.model.LanguageContext
import com.example.quranapp.ui.theme.QuranAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val finalList = mutableListOf<LanguageContext>()
    private val ayahList = mutableListOf<Ayah>()
    private val surahLineList = mutableListOf<AyahModel>()

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
                val newList = mutableListOf<LanguageContext>() // Reset newList for each j

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

        Timber.tag("SurahLineMap").i("${surahLineList[0].ayah.size}")
        Timber.tag("SurahLineMap").i("${surahLineList[0].ayah[6]?.size}")
        Timber.tag("SurahLineMap").i("${surahLineList[0].ayah[7]?.size}")

        setContent {
            QuranAppTheme {
                CategorizedLazyColumn(
                    categories = surahLineList
                )
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                ) {
//                    itemsIndexed(surahNameList) { index, content ->
//                        QuranList(
//                            index = index,
//                            surahNameList = surahNameList,
//                            ayahLineMap = surahLineMap,
//                        )
//                    }
//                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryItem(
    index: Int,
    categories: AyahModel,
    ) {
    FlowRow(
        maxItemsInEachRow = 5,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.ayah[index+1]?.forEach { item ->
            Column(
                modifier = Modifier
                    .background(
                        colorResource(id = R.color.lightGray),
                        shape = RoundedCornerShape(20)
                    )
                    .padding(10.dp),
                ) {
                Text(text = item.arabicLang, textAlign = TextAlign.End)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = item.russianLang, textAlign = TextAlign.End)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Adjust padding as needed
    ) {
        Divider(
            modifier = Modifier
                .height(1.dp), // Adjust height as needed
            color = Color.Gray // Adjust color as needed
        )
    }

//    FlowRow(
//        maxItemsInEachRow = 5,
//        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        Column(
//            modifier = Modifier
//                .background(
//                    colorResource(id = R.color.lightGray),
//                    shape = RoundedCornerShape(20)
//                )
//                .padding(10.dp),
//            horizontalAlignment = Alignment.End,
//
//            ) {
//            Text(text = categories.ayah[index], textAlign = TextAlign.End)
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Text(text = list.russianLang, textAlign = TextAlign.End)
//        }
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategorizedLazyColumn(
    categories: List<AyahModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        categories.forEachIndexed { index, category ->
            // Check if this is the first category or if the previous category is different
            val isFirstCategory = index == 0 || categories[index - 1].nameSurah != category.nameSurah

            if (isFirstCategory) {
                // Display the header for the category
                item {
                    CategoryHeader(category.nameSurah)
                }
            }

            // Display the items for the category
            items(category.ayah.size) { item ->
                CategoryItem(item, category)
            }
        }
    }
}

//@Composable
//private fun listFun(item: LanguageContext) {
//    Column(
//        modifier = Modifier
//            .background(
//                colorResource(id = R.color.lightGray),
//                shape = RoundedCornerShape(20)
//            )
//            .padding(10.dp),
//        horizontalAlignment = Alignment.End,
//
//        ) {
//        Text(text = item.arabicLang, textAlign = TextAlign.End)
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        Text(text = item.russianLang, textAlign = TextAlign.End)
//    }
//}

//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//private fun QuranList(
//    index: Int,
//    surahNameList: List<Surah>,
//    ayahLineMap: List<Ayah>,
//) {
//    Spacer(modifier = Modifier.width(8.dp))
//    Text(
//        text = "${surahNameList[index].name} - ${surahNameList[index].translation}",
//        fontSize = 20.sp,
//        modifier = Modifier.fillMaxWidth(),
//        fontWeight = FontWeight.Bold,
//    )
//
//    FlowRow(
//        maxItemsInEachRow = 5,
//        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        ayahLineMap[index]!!.forEach { item ->
//            Column(
//                modifier = Modifier
//                    .background(
//                        colorResource(id = R.color.lightGray),
//                        shape = RoundedCornerShape(20)
//                    )
//                    .padding(10.dp),
//                horizontalAlignment = Alignment.End,
//
//                ) {
//                Text(text = item.arabicLang, textAlign = TextAlign.End)
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(text = item.russianLang, textAlign = TextAlign.End)
//            }
//        }
//    }

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
//}

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