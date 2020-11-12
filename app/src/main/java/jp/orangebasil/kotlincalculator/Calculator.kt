package jp.orangebasil.kotlincalculator

import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


class Calculator {

    enum class Input {
        ALLCLEAR, CLEAR, ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EQUAL, PERCENT, PLUSMINUS, DECIMAL,
        VALUE_0, VALUE_1, VALUE_2, VALUE_3, VALUE_4, VALUE_5, VALUE_6, VALUE_7, VALUE_8, VALUE_9
    }

    private enum class Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, INVALID
    }

    private val mc: MathContext = MathContext(12, RoundingMode.DOWN)

    private var fixedValue1st: BigDecimal? = null

    private var fixedValue2nd: BigDecimal? = null

    private var editableValueStr: String = "0"

    private var editableOperator: Operator = Operator.INVALID

    private var fixedOperator: Operator = Operator.INVALID

    private var resolvedValue: BigDecimal? = null

    private lateinit var onResult: (result: String) -> Unit

    private lateinit var onActivatedAllClear: (activated: Boolean) -> Unit

    /*
     * Invalid
     * ↓ (1)
     * Started
     * ↓ (2)
     * Input1st ←(7)------------------+
     * ↓ (3)                          |
     * InputOperation ←(6)-+  ←(8)-+  |
     * ↓ (4)               |       |  |
     * Input2nd -----------+       |  |
     * ↓ (5)                       |  |
     * Resolved -------------------+--+
     */
    private enum class State {
        Invalid, Started,
        Input1st, InputOperation, Input2nd, Resolved,
    }

    private var state: State = State.Invalid

    @Throws(IllegalStateException::class)
    private fun switchState(next: State) {
        when {
            // (1)
            (this.state == State.Invalid && next == State.Started) -> {
                this.onResult("0")
            }

            // (2)
            (this.state == State.Started && next == State.Input1st) -> {
                /* do nothing */
            }

            // (3)
            (this.state == State.Input1st && next == State.InputOperation) -> {
                this.fixedValue1st= BigDecimal(this.editableValueStr)
            }

            // (4)
            (this.state == State.InputOperation && next == State.Input2nd) -> {
                this.fixedOperator = this.editableOperator
                this.editableOperator = Operator.INVALID
            }

            // (5)
            (this.state == State.Input2nd && next == State.Resolved) -> {
                this.fixedValue2nd= BigDecimal(this.editableValueStr)
                execute()

                this.editableValueStr = this.resolvedValue.toString()
            }

            // (6)
            (this.state == State.Input2nd && next == State.InputOperation) -> {
                this.fixedValue2nd = BigDecimal(this.editableValueStr)
                execute()

                this.fixedOperator = Operator.INVALID
                this.fixedValue1st = this.resolvedValue
                this.fixedValue2nd = null
                this.resolvedValue = null
            }

            // (7)
            (this.state == State.Resolved && next == State.Input1st) -> {
                this.fixedOperator = Operator.INVALID
                this.fixedValue2nd = null
                this.resolvedValue = null
            }

            // (8)
            (this.state == State.Resolved && next == State.InputOperation) -> {
                this.fixedOperator = Operator.INVALID
                this.fixedValue1st = this.resolvedValue
                this.fixedValue2nd = null
                this.resolvedValue = null
            }

            else -> {
                throw IllegalStateException("current: ${this.state}, next: ${next}")
            }
        }

        this.state = next
    }

    fun start(): Boolean {
        switchState(State.Started)
        this.onActivatedAllClear(true)
        return true
    }

    fun setOnResult(f: (result: String) -> Unit) {
        this.onResult = f
    }

    fun setOnActivatedAllClear(f: (activated: Boolean) -> Unit) {
        this.onActivatedAllClear = f
    }

    fun push(input: Input) {
        when(input) {
            Input.ALLCLEAR -> { this.allClear() }
            Input.CLEAR -> { this.clear() }
            Input.SUBTRACTION -> { this.setOperator(Operator.SUBTRACTION) }
            Input.ADDITION -> { this.setOperator(Operator.ADDITION) }
            Input.DIVISION -> { this.setOperator(Operator.DIVISION) }
            Input.MULTIPLICATION -> { this.setOperator(Operator.MULTIPLICATION) }
            Input.EQUAL -> { this.resolve() }
            Input.PLUSMINUS -> { this.applyFormula { currValue -> currValue.multiply(BigDecimal(-1), mc) } }
            Input.PERCENT -> { this.applyFormula { currValue -> currValue.divide(BigDecimal(100), mc) } }
            Input.DECIMAL -> { this.toDecimal() }
            Input.VALUE_0 -> { this.pushNumber(0) }
            Input.VALUE_1 -> { this.pushNumber(1) }
            Input.VALUE_2 -> { this.pushNumber(2) }
            Input.VALUE_3 -> { this.pushNumber(3) }
            Input.VALUE_4 -> { this.pushNumber(4) }
            Input.VALUE_5 -> { this.pushNumber(5) }
            Input.VALUE_6 -> { this.pushNumber(6) }
            Input.VALUE_7 -> { this.pushNumber(7) }
            Input.VALUE_8 -> { this.pushNumber(8) }
            Input.VALUE_9 -> { this.pushNumber(9) }
        }
    }

    private fun setOperator(o: Operator) {

        this.editableOperator = o

        if (this.state != State.InputOperation)
        {
            switchState(State.InputOperation)
        }
    }

    private fun resolve() {
        if (this.state == State.Input2nd) {
            switchState(State.Resolved)
        }
    }

    private fun pushNumber(v: Int) {
        var tmpStr = "${this.editableValueStr}${v.toString()}"

        if (this.state == State.Started || this.state == State.InputOperation || this.state == State.Resolved) {
            tmpStr = v.toString()
        }

        val afterValue: BigDecimal = BigDecimal(tmpStr)
        val beforeValue: BigDecimal = BigDecimal(this.editableValueStr)
        if (beforeValue.equals(afterValue) || !isSupportedNumber(afterValue)) return

        this.editableValueStr = afterValue.toString()
        this.onResult(this.editableValueStr)

        this.onActivatedAllClear(false)
        switchToInputIfNeeded()
    }

    private fun toDecimal() {
        var tmpStr = this.editableValueStr
        if (!this.editableValueStr.contains(".")) {
            tmpStr += "."
        }

        this.editableValueStr = tmpStr
        this.onResult(this.editableValueStr)

        switchToInputIfNeeded()
    }

    private fun applyFormula(f: (currValue: BigDecimal) -> BigDecimal) {
        var currValue = BigDecimal(this.editableValueStr)
        var nextValue = f(currValue)

        if (!isSupportedNumber(nextValue)) nextValue = BigDecimal(0)

        this.editableValueStr = nextValue.toString()
        this.onResult(this.editableValueStr)

        switchToInputIfNeeded()
    }

    private fun switchToInputIfNeeded() {
        if (this.state == State.Started || this.state == State.Resolved) {
            switchState(State.Input1st)
        } else if (this.state == State.InputOperation) {
            switchState(State.Input2nd)
        }
    }

    private fun isSupportedNumber(n: BigDecimal): Boolean {
        if (n.scale() >= 10) return false;
        if (n > BigDecimal(10000000000)) return false

        return true
    }

    private fun allClear() {
        this.fixedValue1st = null
        this.fixedValue2nd = null
        this.resolvedValue = null
        editableValueStr = "0"
        editableOperator = Operator.INVALID
        fixedOperator = Operator.INVALID
        this.onResult("0")
    }

    private fun clear() {
        this.resolvedValue = null
        editableValueStr = "0"
        this.onResult("0")

        this.onActivatedAllClear(true)
    }

    private fun execute() {

        if (this.fixedOperator == Operator.INVALID ||
            this.fixedValue1st == null || this.fixedValue2nd == null) {
            return
        }

        val resultValue: BigDecimal
        when(this.fixedOperator) {
            Operator.ADDITION -> { resultValue = this.fixedValue1st!!.plus(this.fixedValue2nd!!) }
            Operator.SUBTRACTION -> { resultValue = this.fixedValue1st!!.subtract(this.fixedValue2nd!!, mc)  }
            Operator.MULTIPLICATION -> { resultValue = this.fixedValue1st!!.multiply(this.fixedValue2nd!!, mc) }
            Operator.DIVISION -> { resultValue = this.fixedValue1st!!.divide(this.fixedValue2nd!!, mc) }
            else -> {
                throw IllegalArgumentException("Invalid operation. ${this.fixedOperator}")
            }
        }

        this.resolvedValue = resultValue
        this.onResult(resultValue.toPlainString())
    }
}
