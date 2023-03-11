package br.projeto.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import br.projeto.calculadora.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var lastNumeric = false
    var statError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onAllClearClick(view: View) {
        binding.tvData.text = ""
        binding.tvResult.text = ""
        statError = false
        lastDot = false
        lastNumeric = false
        binding.tvResult.visibility = View.GONE
    }

    fun onDigitClick(view: View) {
        if (statError) {
            binding.tvData.text = (view as Button).text
            statError = false
        } else {
            binding.tvData.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.tvData.text = binding.tvResult.text.toString().drop(1)
    }

    fun onOperatorClick(view: View) {
        if (!statError && lastNumeric) {
            binding.tvData.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onBackClick(view: View) {
        binding.tvData.text = binding.tvData.text.toString().dropLast(1)
        try {
            val lastChar = binding.tvData.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {
        binding.tvData.text = ""
        lastNumeric = false
    }

    fun onEqual() {
        if (lastNumeric && !statError) {
            val txt = binding.tvData.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = "=" + result.toString()
            } catch (ex: java.lang.ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.tvResult.text = "Error"
                statError = true
                lastNumeric = false
            }
        }
    }
}