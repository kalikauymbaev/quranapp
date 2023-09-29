package com.example.quranapp.mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranapp.model.AyahModel
import com.example.quranapp.ui.theme.QuranAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getFinalList()
        bindViewModel()
    }

    private fun bindViewModel() = with(viewModel) {
        finalLiveData.observe(this@MainActivity) { list ->
            showContent(list)
        }
    }

    private fun showContent(surahLineList: List<AyahModel>) {
        setContent {
            QuranAppTheme {
                CategorizedLazyColumn(
                    categories = surahLineList
                )
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
        categories.ayah[index + 1]?.forEach { item ->
            Column(
                modifier = Modifier
                    .background(
                        Color.LightGray,
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
            .padding(vertical = 8.dp)
    ) {
        Divider(
            modifier = Modifier
                .height(1.dp),
            color = Color.Gray
        )
    }
}

@Composable
private fun CategorizedLazyColumn(
    categories: List<AyahModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        categories.forEachIndexed { index, category ->
            val isFirstCategory =
                index == 0 || categories[index - 1].nameSurah != category.nameSurah

            if (isFirstCategory) {
                item {
                    CategoryHeader(category.nameSurah)
                }
            }

            items(category.ayah.size) { item ->
                CategoryItem(item, category)
            }
        }
    }
}
