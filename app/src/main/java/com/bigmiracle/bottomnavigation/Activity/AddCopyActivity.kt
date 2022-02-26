package com.bigmiracle.bottomnavigation.Activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.databinding.ActivityAddCopyBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class AddCopyActivity : AppCompatActivity() {

    private var binding: ActivityAddCopyBinding? = null
//
//    private lateinit var type: String
//    private  lateinit var subtype: String

    private var type: String = "現股"
    private var subtype: String = "買進"

    private var volume: Double = 0.0
    private var price: Double = 0.0
//
//    companion object {
//        private var type: String = "現股"
//        private var subtype: String = "買進"
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCopyBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.noteEditText?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding?.sv?.scrollTo(0, binding!!.sv.bottom)
            }
        }
        binding?.dateTextView?.setOnClickListener {
            showDatePicker()

        }

        setupActionBar()

       binding?.doneButton?.setOnClickListener {

           var amount = binding?.amountEditText?.text




           Toast.makeText(this, "$amount", Toast.LENGTH_SHORT).show()

       }

        showToday()
        transactionTypeCheck()
        volumeButtonClick()
        calculateOnTextChanged()

    }


    private fun transactionTypeCheck(){
        binding?.typeToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if(isChecked){
                if(checkedId == binding?.type1Button?.id){

                    type = "現股"
                    subtype = "買進"

                    binding?.subtype1Button?.text = "買進"
                    binding?.subtype2Button?.text = "賣出"

                    binding?.subtype1Button?.let { binding?.subtypeToggleGroup?.check(it.id) }

                    Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()

                } else if(checkedId == binding?.type2Button?.id){

                    type = "股利"
                    subtype = "現金股利"

                    binding?.subtype1Button?.text = "現金股利"
                    binding?.subtype2Button?.text = "股票股利"

                    binding?.subtype1Button?.let { binding?.subtypeToggleGroup?.check(it.id) }


                    Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()

                }
            }
        }
        binding?.subtypeToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if(checkedId == binding?.subtype1Button?.id){
                    if(type == "現股"){
                        subtype = "買進"
                        Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()

                        binding?.feeEditText?.visibility = View.GONE
                    } else if(type == "股利") {
                        subtype = "現金股利"
                        Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()
                    }

                } else if (checkedId == binding?.subtype2Button?.id) {
                    if(type == "現股"){
                        type = "現股"
                        subtype = "賣出"
                        binding?.feeEditText?.visibility = View.VISIBLE
                        Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()

                    } else if (type == "股利") {
                        type = "股利"
                        subtype = "股票股利"
                        Toast.makeText(this, "$type $subtype", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun calculateOnTextChanged(){

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateAmountAndFee()
            }
        }
        binding?.volumeEditText?.addTextChangedListener(textWatcher)
        binding?.priceEditText?.addTextChangedListener(textWatcher)
    }



    private fun calculateAmountAndFee(){

        var volumeString = binding?.volumeEditText?.text.toString()
        var priceString = binding?.priceEditText?.text.toString()

        if( !volumeString.isNullOrEmpty()&& !priceString.isNullOrEmpty()){

            var numberFormat = NumberFormat.getIntegerInstance()
            numberFormat.maximumFractionDigits = 0
            volumeString =numberFormat.parse(volumeString).toString()

            volume = volumeString.toDouble()
            price = priceString.toDouble()
            var feeDiscount = 0.65
            var lessFee: Double = 1.0
            var amount: Double
            var fee = (volume*price)*0.1425*0.01*feeDiscount

            if(fee < 1) {
                fee = lessFee
            }
            if(volume*price>0){
                amount = volume*price - fee
            } else {
                amount = 0.0
                fee = 0.0
            }


//            binding?.amountEditText?.setText(String.format("$%,.0f",amount))
            binding?.feeEditText?.setText(String.format("$%,.0f",fee))

            var dollarFormat = NumberFormat.getCurrencyInstance()
            dollarFormat.maximumFractionDigits = 0
            binding?.amountEditText?.setText(dollarFormat.format(amount))


        } else {
            binding?.amountEditText?.setText("0")
            binding?.feeEditText?.setText("0")

        }
    }

    private fun volumeButtonClick(){

        binding?.minusButton?.setOnClickListener {
            if(volume >= 1000){
                volume -= 1000
            } else if(volume<1000) {
                volume = 0.0
            }


            var numberFormat = NumberFormat.getIntegerInstance()
            numberFormat.maximumFractionDigits = 0
            binding?.volumeEditText?.setText(numberFormat.format(volume))

//            binding?.volumeEditText?.setText(volume.toInt().toString())
            calculateAmountAndFee()
        }

        binding?.plusButton?.setOnClickListener {
            volume += 1000
            binding?.volumeEditText?.setText(volume.toInt().toString())
            calculateAmountAndFee()
        }

    }



    private fun showDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val storeDayOfMonth = if(dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val storeMonthOfYear = if((monthOfYear + 1)<10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val selectedDate = "$year/$storeMonthOfYear/$storeDayOfMonth"

                binding?.dateTextView?.setText(selectedDate)

//                val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
//                val theDate = simpleDateFormat.parse(selectedDate)
////                selectedDueDateMilliSeconds = theDate!!.time
            }, year, month, day

        )
        datePickerDialog.show()
    }

    private fun showToday(){

        var simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
        var todayDate = simpleDateFormat.format(System.currentTimeMillis())
        binding?.dateTextView?.setText(todayDate)

    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarAddActivity)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.icon_arrow_back)
            supportActionBar!!.title="新增"
        }
        binding?.toolbarAddActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

