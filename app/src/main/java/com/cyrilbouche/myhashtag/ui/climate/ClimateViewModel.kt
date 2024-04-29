package com.cyrilbouche.myhashtag.ui.climate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClimateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _temperature = MutableLiveData<String>().apply {
        value = "--°C"
    }
    var temperature: LiveData<String> = _temperature

    private val _gauge = MutableLiveData<String>().apply {
        value = ""
    }
    val gauge: LiveData<String> = _gauge

    private val _activePreset = MutableLiveData<Int>().apply {
        value = null
    }
    val activePreset: LiveData<Int> = _activePreset

    fun updateActivePreset(newValue: Int) {
        _activePreset.value = newValue
    }
}