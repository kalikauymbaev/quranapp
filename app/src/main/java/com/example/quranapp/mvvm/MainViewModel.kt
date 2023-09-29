package com.example.quranapp.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quranapp.model.AyahModel
import com.example.quranapp.repositories.ListofQuranRepository
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val listofQuranRepository: ListofQuranRepository,
) : AndroidViewModel(application) {

    val finalLiveData: MutableLiveData<List<AyahModel>> = MutableLiveData()

    fun getFinalList() {
        viewModelScope.launch {
            finalLiveData.postValue(listofQuranRepository.getListOfFullQuran())
        }
    }
}
