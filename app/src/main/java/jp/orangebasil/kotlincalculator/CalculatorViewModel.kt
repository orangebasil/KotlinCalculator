package jp.orangebasil.kotlincalculator

import androidx.lifecycle.MutableLiveData

class CalculatorViewModel {
    val resultText: MutableLiveData<String> = MutableLiveData<String>().also {
        it.value = "0"
    }

    val activatedAllClear: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = true
    }
}