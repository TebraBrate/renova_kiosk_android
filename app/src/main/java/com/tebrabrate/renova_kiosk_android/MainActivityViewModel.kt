package com.tebrabrate.renova_kiosk_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() :
    ViewModel() {

    private val _events = MutableLiveData<String>()

    val events: LiveData<String>
        get() = _events

}
