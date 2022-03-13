package com.bigmiracle.bottomnavigation.Activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import com.bigmiracle.bottomnavigation.Adapter.stockListAdapter
import com.bigmiracle.bottomnavigation.Data.Datasource
import com.bigmiracle.bottomnavigation.Database.Record.RecordDao
import com.bigmiracle.bottomnavigation.Database.Record.RecordEntity
import com.bigmiracle.bottomnavigation.Database.RecordApplication
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.Utils
import com.bigmiracle.bottomnavigation.databinding.ActivityAddBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddActivity : BaseActivity() {

    private var binding: ActivityAddBinding? = null

    private var stock: String = ""
    private var type: String = "現股"
    private var subtype: String = "買進"
    private var transactionType: Int = 1
    private var date: String = ""

    private var price: Double = 0.0
    private var shares: Int = 0
    private var fee: Int = 0
    private var tax: Int = 0
    private var amount: Int = 0
    private var distributeShares: Int = 0
    private var value: Double = 0.0

    private var feeDiscount = 0.65
    private var minimumFee: Int = 20

    private var autoCalculate: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.dateTextView?.setOnClickListener {
            showDatePicker()
        }

        //輸入股名股號
        val stockList = Datasource().loadAllStocks()
        val adapter = stockListAdapter(this, R.layout.adapter_search_list_item, ArrayList(stockList))

        binding?.stockAutoCompleteTextView?.threshold = 1
        binding?.stockAutoCompleteTextView?.setAdapter(adapter)


        setupActionBar()
        showToday()
        transactionTypeCheck()
        scrollWhenPressEditText()

        typingOnPriceChanged()
        typingOnSharesChanged()
        typingOnFeeChanged()
        typingOnTaxChanged()
        typingOnAmountChanged()
        typingOnDistributeSharesChanged()

        sharesButtonClick()
        doneButtonClickable()

    }

    private fun scrollWhenPressEditText(){
        binding?.noteEditText?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding?.sv?.scrollTo(0, binding!!.sv.bottom)
            }
        }

        binding?.feeEditText?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding?.sv?.scrollTo(0, binding!!.sv.height/4)
            }
        }

        binding?.taxEditText?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding?.sv?.scrollTo(0, binding!!.sv.height/4)
            }
        }

        binding?.amountEditText?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding?.sv?.scrollTo(0, binding!!.sv.height/4)
            }
        }
    }

    //股價
    private fun typingOnPriceChanged(){

        binding?.priceEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.priceEditText?.removeTextChangedListener(this)
                var priceString = binding?.priceEditText?.text.toString()
                if (!TextUtils.isEmpty(priceString)) {
                    price = binding?.priceEditText?.text.toString().toDouble()

                } else {
                    price = 0.0
                }

                binding?.priceEditText?.addTextChangedListener(this)
                calculate()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    //股數
    private fun typingOnSharesChanged(){

        binding?.sharesEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.sharesEditText?.removeTextChangedListener(this)
                shares = Utils.thousandFormat(binding?.sharesEditText!!).toInt()
                binding?.sharesEditText?.addTextChangedListener(this)
                calculate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //手續費
    private fun typingOnFeeChanged(){

        binding?.feeEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.feeEditText?.removeTextChangedListener(this)
                fee = Utils.thousandFormat(binding?.feeEditText!!).toInt()
                binding?.feeEditText?.addTextChangedListener(this)

                if(!autoCalculate){
                    if(type == "現股"&& subtype == "買進"){
                        amount = (price*shares.toDouble()).toInt()+fee
                        binding?.amountEditText?.setText(Utils.numberFormat(amount))
                    } else if (type == "現股"&& subtype == "賣出"){
                        amount = (price*shares.toDouble()).toInt()-fee-tax
                        binding?.amountEditText?.setText(Utils.numberFormat(amount))

                    } else if (type == "股利"&& subtype == "現金股利"){
                        amount = (price*shares.toDouble()).toInt()-fee
                        binding?.amountEditText?.setText(Utils.numberFormat(amount))

                    }
                }
                doneButtonClickable()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //交易稅
    private fun typingOnTaxChanged(){

        binding?.taxEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.taxEditText?.removeTextChangedListener(this)
                tax = Utils.thousandFormat(binding?.taxEditText!!).toInt()
                binding?.taxEditText?.addTextChangedListener(this)

                if(!autoCalculate){
                    amount = (price*shares.toDouble()).toInt()-fee-tax
                    binding?.amountEditText?.setText(Utils.numberFormat(amount))
                }

                doneButtonClickable()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //交易金額
    private fun typingOnAmountChanged(){

        binding?.amountEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.amountEditText?.removeTextChangedListener(this)
                amount = Utils.thousandFormat(binding?.amountEditText!!).toInt()
                binding?.amountEditText?.addTextChangedListener(this)

                doneButtonClickable()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //配發股數
    private fun typingOnDistributeSharesChanged(){

        binding?.distributeSharesEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding?.distributeSharesEditText?.removeTextChangedListener(this)
                distributeShares = Utils.thousandFormat(binding?.distributeSharesEditText!!).toInt()
                binding?.distributeSharesEditText?.addTextChangedListener(this)

                doneButtonClickable()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun calculate(){

        autoCalculate = true

        value = price*shares

        if(value>0){

            if(type=="現股" && subtype=="買進"){

                fee = (value*0.001425*feeDiscount).toInt()

                if(fee < minimumFee){
                    fee = minimumFee
                }

                amount =(value + fee).toInt()

            } else if (type=="現股" && subtype=="賣出"){

                fee = (value*0.001425*feeDiscount).toInt()

                if(fee < minimumFee){
                    fee = minimumFee
                }

                tax = (value*0.003).toInt()

                amount = (value-fee-tax).toInt()

            } else if (type=="股利" && subtype=="現金股利"){
                fee = 10
                amount =(value - fee).toInt()

            } else if (type=="股利" && subtype=="股票股利"){
                distributeShares = (price/10.0*shares).toInt()
            }


        } else {
            fee = 0
            tax = 0
            amount = 0
            distributeShares = 0

        }

        showDataIntoEdittext()
        autoCalculate = false
    }

    fun showDataIntoEdittext(){

        binding?.feeEditText?.setText(Utils.numberFormat(fee))
        binding?.amountEditText?.setText(Utils.numberFormat(amount))
        binding?.taxEditText?.setText(Utils.numberFormat(tax))
        binding?.distributeSharesEditText?.setText(Utils.numberFormat(distributeShares))

        doneButtonClickable()
    }

    private fun sharesButtonClick(){

        binding?.minusButton?.setOnClickListener {
            if(shares >= 1000){
                shares -= 1000
            } else if(shares<1000) {
                shares = 0
            }
            binding?.sharesEditText?.setText(Utils.numberFormat(shares))

        }

        binding?.plusButton?.setOnClickListener {
            shares += 1000
            binding?.sharesEditText?.setText(Utils.numberFormat(shares))
        }
    }

    private fun addRecord(recordDao: RecordDao){

        val stockId: String
        val stockName: String
        val splitString = stock.split(" ")
        stockId = splitString[0]
        stockName = splitString[1]

        lifecycleScope.launch {
            recordDao.insert(RecordEntity(stockId = stockId, stockName = stockName, transactionType = transactionType, price = price, share = shares, fee = fee, tax = tax, distributionShares = distributeShares,amount = amount))
        }

        Utils.showToast(this@AddActivity,"新增紀錄成功")
        finish()

    }


    private fun doneButtonClickable(){

        stock = binding?.stockAutoCompleteTextView?.text.toString()
        val recordDao = (application as RecordApplication).database.recordDao()


        if(type=="現股" && subtype=="買進"){
            if(stock != "" && price != 0.0 && amount != 0 && fee != 0 && shares != 0){

                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    amount = amount*-1
                    addRecord(recordDao)
                }

            } else {
                binding?.doneButton?.isEnabled = false
            }

        } else if (type=="現股" && subtype=="賣出"){

            if(stock != "" && price != 0.0 && amount != 0 && fee != 0 && shares != 0 && tax!=0 ){

                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    addRecord(recordDao)
                }

            } else {
                binding?.doneButton?.isEnabled = false
            }

        } else if (type=="股利" && subtype=="現金股利"){
            if(stock != "" && amount != 0){
                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    addRecord(recordDao)
                }
            } else {
                binding?.doneButton?.isEnabled = false
            }
        } else if (type=="股利" && subtype=="股票股利"){
            if(stock != "" && distributeShares != 0){
                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    addRecord(recordDao)
                }
            } else {
                binding?.doneButton?.isEnabled = false
            }

        }

    }

    @TypeConverter
    fun fromTimestamp(timeStamp: Long?): String? {
        return timeStamp?.let { FORMATTER.format(timeStamp) }
    }

    @TypeConverter
    fun dateToTimestamp(timeStamp: String?): Long? {
        return timeStamp?.let { FORMATTER.parse(it)?.time }
    }

    companion object{

        val FORMATTER = SimpleDateFormat("yyyy/MM/dd")
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

    private fun transactionType(){
        if(type =="現股" && subtype =="買進"){
            transactionType = 1
        } else if (type =="現股" && subtype =="賣出"){
            transactionType = 2
        } else if (type =="股利" && subtype =="現金股利"){
            transactionType = 3
        } else if(type =="股利" && subtype =="股票股利"){
            transactionType = 4
        }
    }

    private fun transactionTypeCheck(){
        binding?.typeToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if(isChecked){
                if(checkedId == binding?.type1Button?.id){

                    type = "現股"
                    subtype = "買進"
                    transactionType()

                    binding?.subtype1Button?.text = "買進"
                    binding?.subtype2Button?.text = "賣出"

                    binding?.subtype1Button?.let { binding?.subtypeToggleGroup?.check(it.id) }

                    binding?.amountLinearLayout?.visibility = View.VISIBLE
                    binding?.feeLinearLayout?.visibility = View.VISIBLE
                    binding?.taxLinearLayout?.visibility = View.GONE
                    binding?.distributeLinearLayout?.visibility = View.GONE

                    binding?.priceTextView?.text = "價格"
                    binding?.sharesTextView?.text = "股數"
                    binding?.distributeSharesTextView?.text = "交易金額"

                } else if(checkedId == binding?.type2Button?.id){

                    type = "股利"
                    subtype = "現金股利"
                    transactionType()

                    binding?.subtype1Button?.text = "現金股利"
                    binding?.subtype2Button?.text = "股票股利"

                    binding?.subtype1Button?.let { binding?.subtypeToggleGroup?.check(it.id) }

                    binding?.amountLinearLayout?.visibility = View.VISIBLE
                    binding?.feeLinearLayout?.visibility = View.VISIBLE
                    binding?.taxLinearLayout?.visibility = View.GONE
                    binding?.distributeLinearLayout?.visibility = View.GONE

                    binding?.priceTextView?.text = "現金股利"
                    binding?.sharesTextView?.text = "持有股數"
                    binding?.distributeSharesTextView?.text = "配發金額"


                }

                Utils.showToast(this,"$type $subtype")
                cleanNumber()
            }
        }
        binding?.subtypeToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->


            if(isChecked){
                if(checkedId == binding?.subtype1Button?.id){
                    if(type == "現股"){
                        subtype = "買進"
                        transactionType()

                        binding?.amountLinearLayout?.visibility = View.VISIBLE
                        binding?.feeLinearLayout?.visibility = View.VISIBLE
                        binding?.taxLinearLayout?.visibility = View.GONE
                        binding?.distributeLinearLayout?.visibility = View.GONE

                        binding?.priceTextView?.text = "價格"
                        binding?.sharesTextView?.text = "股數"
                        binding?.distributeSharesTextView?.text = "交易金額"

                    } else if(type == "股利") {
                        subtype = "現金股利"
                        transactionType()

                        binding?.amountLinearLayout?.visibility = View.VISIBLE
                        binding?.feeLinearLayout?.visibility = View.VISIBLE
                        binding?.taxLinearLayout?.visibility = View.GONE
                        binding?.distributeLinearLayout?.visibility = View.GONE

                        binding?.priceTextView?.text = "現金股利"
                        binding?.sharesTextView?.text = "持有股數"
                        binding?.distributeSharesTextView?.text = "配發金額"

                    }

                } else if (checkedId == binding?.subtype2Button?.id) {
                    if(type == "現股"){
                        type = "現股"
                        subtype = "賣出"
                        transactionType()

                        binding?.amountLinearLayout?.visibility = View.VISIBLE
                        binding?.feeLinearLayout?.visibility = View.VISIBLE
                        binding?.taxLinearLayout?.visibility = View.VISIBLE
                        binding?.distributeLinearLayout?.visibility = View.GONE

                        binding?.priceTextView?.text = "價格"
                        binding?.sharesTextView?.text = "股數"
                        binding?.distributeSharesTextView?.text = "交易金額"


                    } else if (type == "股利") {
                        type = "股利"
                        subtype = "股票股利"
                        transactionType()

                        binding?.amountLinearLayout?.visibility = View.GONE
                        binding?.feeLinearLayout?.visibility = View.GONE
                        binding?.taxLinearLayout?.visibility = View.GONE
                        binding?.distributeLinearLayout?.visibility = View.VISIBLE

                        binding?.priceTextView?.text = "股票股利"
                        binding?.sharesTextView?.text = "持有股數"
                        binding?.distributeSharesTextView?.text = "配發股數"

                    }
                }
                Utils.showToast(this,"$type $subtype")
                cleanNumber()
            }
        }
    }

    private fun cleanNumber(){
        fee = 0
        price = 0.0
        tax = 0
        amount = 0
        shares = 0
        distributeShares = 0

        binding?.feeEditText?.setText("")
        binding?.amountEditText?.setText("")
        binding?.taxEditText?.setText("")
        binding?.priceEditText?.setText("")
        binding?.sharesEditText?.setText("")
        binding?.distributeSharesEditText?.setText("")
    }

}


