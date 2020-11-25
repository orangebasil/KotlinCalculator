package jp.orangebasil.kotlincalculator

import androidx.lifecycle.MutableLiveData


interface ICalculatorModel {
    fun setResultText(text: String)

    fun getResultText(): String

    fun setActivatedAllClear(activated: Boolean)

    fun activatedAllClear(): Boolean
}


class CalculatorViewModel: ICalculatorModel {
    val resultTextAsLD: MutableLiveData<String> = MutableLiveData<String>().also {
        it.value = "0"
    }

    val activatedAllClearAsLD: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = true
    }

    override fun setResultText(text: String) {
        resultTextAsLD.value = text
    }

    override fun getResultText(): String {
        return this.resultTextAsLD.value as String
    }

    override fun setActivatedAllClear(activated: Boolean) {
        this.activatedAllClearAsLD.value = activated
    }

    override fun activatedAllClear(): Boolean {
        return this.activatedAllClearAsLD.value as Boolean
    }
}