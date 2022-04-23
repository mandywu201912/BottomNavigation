package com.bigmiracle.bottomnavigation

import android.app.Activity
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import java.text.DecimalFormat
import java.text.NumberFormat

object Utils {

    fun dollarFormat(number: Double) : String {
        var dollarFormat = NumberFormat.getCurrencyInstance()
        dollarFormat.maximumFractionDigits = 0
        return dollarFormat.format(number)
    }

    fun dollarFormatParse(text: String) : Double {
        var dollarFormat = NumberFormat.getCurrencyInstance()
        var result: Double = 0.0
        dollarFormat.maximumFractionDigits = 0

        if(!text.isNullOrEmpty()){
            result = dollarFormat.parse(text).toDouble()
        } else if (text.isNullOrEmpty() || text =="0") {
            result = 0.0
        }
        return result
    }



    fun numberFormatParseInt(text: String) : Int {
        var numberFormat = NumberFormat.getIntegerInstance()
        var result: Int = 0
        numberFormat.maximumFractionDigits = 0

        if(!text.isNullOrEmpty()){
            result = numberFormat.parse(text).toInt()
        } else if (text.isNullOrEmpty() || text =="0") {
            result = 0
        }
        return result
    }

    fun numberFormatParseDouble(text: String) : Double {
        var numberFormat = NumberFormat.getNumberInstance()
        var result: Double = 0.0

        if(!text.isNullOrEmpty()){
            result = numberFormat.parse(text).toDouble()
        } else if (text.isNullOrEmpty() || text =="0") {
            result = 0.0
        }
        return result
    }

    fun twoDigitDecimalFormat(number: Double): String {
        val decimalFormat = DecimalFormat("0.##")
        return decimalFormat.format(number)
    }







    fun numberFormat(number: Int) : String {
        var numberFormat = NumberFormat.getIntegerInstance()
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(number)
    }


    fun thousandFormat(editText: EditText): Double {
        var string = editText.text.toString()
        var thousandformat = DecimalFormat("#,###")
        var number:Double =0.0
        if (!TextUtils.isEmpty(string)) {
            val textWithoutComma = string.replace(",".toRegex(), "")
            number = textWithoutComma.toDouble()
            editText.setText(thousandformat.format(number))
            editText.setSelection(thousandformat.format(number).length)
        }
        return number
    }

    fun showToast(activity: Activity, text: String){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()

    }
}