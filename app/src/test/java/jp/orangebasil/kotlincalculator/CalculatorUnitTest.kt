package jp.orangebasil.kotlincalculator

import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class CalculatorUnitTest {
    @Test
    fun isSupportedNumber() {
        var model = object : ICalculatorModel {
            private var _resultText: String = "0"
            private var _activatedAllClear: Boolean = false
            override fun setResultText(text: String) { _resultText = text }
            override fun getResultText(): String { return _resultText }
            override fun setActivatedAllClear(activated: Boolean) { _activatedAllClear = activated }
            override fun activatedAllClear(): Boolean { return _activatedAllClear }
        }
        var calculator = Calculator(model)

        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("0")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("10000000000")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("-10000000000")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("0.000000001")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("-0.000000001")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("9999999999.999999999")))
        Assert.assertTrue(calculator.isSupportedNumber(BigDecimal("-9999999999.999999999")))

        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("10000000001")))
        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("-10000000001")))
        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("0.0000000001")))
        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("-0.0000000001")))
        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("10000000000.0000000001")))
        Assert.assertFalse(calculator.isSupportedNumber(BigDecimal("-10000000000.0000000001")))
    }
}