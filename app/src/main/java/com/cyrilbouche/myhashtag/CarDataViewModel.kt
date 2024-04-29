package com.cyrilbouche.myhashtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CarDataViewModel : ViewModel() {
    private val _data = MutableLiveData<Vehicle>()
    val data: LiveData<Vehicle> get() = _data

    fun setCarData(vehicle: Vehicle){
        _data.value = vehicle
    }
}