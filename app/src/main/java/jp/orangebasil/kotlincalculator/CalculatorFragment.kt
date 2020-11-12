package jp.orangebasil.kotlincalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class CalculatorFragment : Fragment() {

    private val calculator = Calculator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textResult = this.view!!.findViewById<TextView>(R.id.textview_result)
        calculator.setOnResult { textResult.setText(it) }

        var buttonAllClear: Button = this.view!!.findViewById<Button>(R.id.button_allclear)
        var buttonClear: Button = this.view!!.findViewById<Button>(R.id.button_clear)

        buttonAllClear.setOnClickListener { this.calculator.push(Calculator.Input.ALLCLEAR) }
        buttonClear.setOnClickListener { this.calculator.push(Calculator.Input.CLEAR) }

        calculator.setOnActivatedAllClear {
            if (it) {
                buttonAllClear.visibility = View.VISIBLE
                buttonClear.visibility = View.INVISIBLE
            } else {
                buttonAllClear.visibility = View.INVISIBLE
                buttonClear.visibility = View.VISIBLE
            }
        }

        this.view!!.findViewById<Button>(R.id.button_division).setOnClickListener { this.calculator.push(Calculator.Input.DIVISION) }
        this.view!!.findViewById<Button>(R.id.button_multiplication).setOnClickListener { this.calculator.push(Calculator.Input.MULTIPLICATION) }
        this.view!!.findViewById<Button>(R.id.button_addition).setOnClickListener { this.calculator.push(Calculator.Input.ADDITION) }
        this.view!!.findViewById<Button>(R.id.button_subtraction).setOnClickListener { this.calculator.push(Calculator.Input.SUBTRACTION) }

        this.view!!.findViewById<Button>(R.id.button_equal).setOnClickListener { this.calculator.push(Calculator.Input.EQUAL) }
        this.view!!.findViewById<Button>(R.id.button_percent).setOnClickListener { this.calculator.push(Calculator.Input.PERCENT) }
        this.view!!.findViewById<Button>(R.id.button_plusminus).setOnClickListener { this.calculator.push(Calculator.Input.PLUSMINUS) }
        this.view!!.findViewById<Button>(R.id.button_decimal).setOnClickListener { this.calculator.push(Calculator.Input.DECIMAL) }

        this.view!!.findViewById<Button>(R.id.button_0).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_0) }
        this.view!!.findViewById<Button>(R.id.button_1).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_1) }
        this.view!!.findViewById<Button>(R.id.button_2).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_2) }
        this.view!!.findViewById<Button>(R.id.button_3).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_3) }
        this.view!!.findViewById<Button>(R.id.button_4).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_4) }
        this.view!!.findViewById<Button>(R.id.button_5).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_5) }
        this.view!!.findViewById<Button>(R.id.button_6).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_6) }
        this.view!!.findViewById<Button>(R.id.button_7).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_7) }
        this.view!!.findViewById<Button>(R.id.button_8).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_8) }
        this.view!!.findViewById<Button>(R.id.button_9).setOnClickListener { this.calculator.push(Calculator.Input.VALUE_9) }

        calculator.start()
    }
}