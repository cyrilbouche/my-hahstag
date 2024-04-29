package com.cyrilbouche.myhashtag.ui.charge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChargeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is charge Fragment"
    }
    val text: LiveData<String> = _text
}