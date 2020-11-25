package jp.orangebasil.kotlincalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import jp.orangebasil.kotlincalculator.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding

    private val viewModel: CalculatorViewModel = CalculatorViewModel()

    private val calculator = Calculator(this.viewModel)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        binding.vm = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.activatedAllClearAsLD.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.buttonAllclear.visibility = View.VISIBLE
                binding.buttonClear.visibility = View.INVISIBLE
            } else {
                binding.buttonAllclear.visibility = View.INVISIBLE
                binding.buttonClear.visibility = View.VISIBLE
            }
        })

        this.view!!.findViewById<Button>(R.id.button_allclear).setOnClickListener { this.calculator.push(Calculator.Input.ALLCLEAR) }
        this.view!!.findViewById<Button>(R.id.button_clear).setOnClickListener { this.calculator.push(Calculator.Input.CLEAR) }

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