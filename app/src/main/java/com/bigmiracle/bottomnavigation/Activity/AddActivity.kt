package com.bigmiracle.bottomnavigation.Activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.bigmiracle.bottomnavigation.Adapter.stockListAdapter
import com.bigmiracle.bottomnavigation.Data.Datasource
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.Database.RecordEntity
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.Utils
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.ActivityAddBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddActivity : BaseActivity() {

    lateinit var recordEntity: RecordEntity

    private val viewModel: DataViewModel by viewModels {
        DataViewModelFactory(
            (this.application as Application).database.dataDao()
        )
    }


    private var binding: ActivityAddBinding? = null

    private var stock: String = ""
    private var stockId: String = ""
    private var stockName: String = ""

    private var type: String = "現股"
    private var subtype: String = "買進"
    private var transactionTypeCode: Int = 1
    private var transactionType:String = "現股買進"
    private var date: String = ""

    private var price: Double = 0.0
    private var shares: Int = 0
    private var fee: Int = 0
    private var tax: Int = 0
    private var amount: Int = 0
    private var income: Int = 0
    private var outcome: Int = 0
    private var distributeShares: Int = 0
    private var value: Double = 0.0

    private var holdingShare: Int = 0

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

    private fun stockIdAndStockName(){

        stock = binding?.stockAutoCompleteTextView?.text.toString()

        if(stock.isNotEmpty()){
            val splitString = stock.split(" ")
            stockId = splitString[0]
            stockName = splitString[1]
        }

    }


    //現股買進viewModel
    private fun isBuyEntryValid(): Boolean {
        return viewModel.isBuyEntryValid(
            stockId,stockName,price,shares,fee,outcome
        )
    }





    private fun addBuyRecord(){
        if(isBuyEntryValid()){

            var averagePrice = outcome.toDouble()/shares.toDouble()
            viewModel.addNewRecord(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,tax,outcome,income,distributeShares,date)
            viewModel.addNewHolding(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,outcome,averagePrice,date)

        }
    }

    //現股賣出viewModel
    private fun isSellEntryValid(): Boolean {
        return viewModel.isSellEntryValid(
            stockId,stockName,price,shares,fee,income,tax
        )
    }

    private fun calculateProfit(){

        var holdingOutcome: Int = 0

        var profit: Int = 0
        var profitRatio: Double = 0.0

        profit = income - holdingOutcome
        profitRatio = profit.toDouble() / holdingOutcome.toDouble()

    }



    private fun addSellRecord(){
        if(isSellEntryValid()){
            val buyAveragePrice = 0.0
            val buyFee = 0
            val profit = 0
            val profitRatio = 0.0

            viewModel.addNewRecord(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,tax,outcome,income,distributeShares,date)
            viewModel.addNewClosed(stockId, stockName, transactionTypeCode,transactionType,buyAveragePrice,shares,buyFee,outcome,price,fee,tax,income,profit,profitRatio,date)

        }
    }

    //現金股利viewModel
    private fun isCashDividendEntryValid(): Boolean {
        return viewModel.isCashDividendEntryValid(
            stockId,stockName,income
        )
    }

    private fun addCashDividendRecord(){
        if(isCashDividendEntryValid()){
            viewModel.addNewRecord(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,tax,outcome,income,distributeShares,date)

        }
    }

    //股票股利viewModel
    private fun isStockDividendEntryValid(): Boolean {
        return viewModel.isStockDividendEntryValid(
            stockId,stockName,distributeShares
        )
    }

    private fun addStockDividendRecord(){
        if(isStockDividendEntryValid()){
            var averagePrice = outcome.toDouble()/shares.toDouble()
            viewModel.addNewRecord(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,tax,outcome,income,distributeShares,date)
            viewModel.addNewHolding(stockId,stockName,transactionTypeCode,transactionType,price,shares,fee,outcome,averagePrice,date)

        }
    }

    private fun showHoldingShares(){
        stockIdAndStockName()
        if(stockId != null && transactionTypeCode == 2){

            lifecycle.coroutineScope.launch {
                viewModel.getSumSharesByStockId(stockId).collect { sum ->
                    holdingShare = sum
                    binding?.holdingSharesText?.text = if(sum != 0){sum.toString()} else {"沒"}
                }
            }

        }
    }



    private fun doneButtonClickable(){

        stockIdAndStockName()


        if(type=="現股" && subtype=="買進"){
            if(stock != "" && price != 0.0 && amount != 0 && fee != 0 && shares != 0){

                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    outcome = amount
                    addBuyRecord()
                    addRecordSuccess()

                }

            } else {
                binding?.doneButton?.isEnabled = false
            }

        } else if (type=="現股" && subtype=="賣出"){

            if(stock != "" && price != 0.0 && amount != 0 && fee != 0 && shares != 0 && tax!=0 ){

                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    income = amount

                    lifecycle.coroutineScope.launch {
                        viewModel.getSumSharesByStockId(stockId).collect { sum ->
                            var holdingShare = sum
                            if(holdingShare >= shares){
                                addSellRecord()
                                calculateProfit()
                                addRecordSuccess()
                            } else {
                                Utils.showToast(this@AddActivity, "no enough holding for sell")
                            }
                        }
                    }



                }

            } else {
                binding?.doneButton?.isEnabled = false
            }

        } else if (type=="股利" && subtype=="現金股利"){
            if(stock != "" && amount != 0){
                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    income = amount
                    addCashDividendRecord()
                    addRecordSuccess()
                }
            } else {
                binding?.doneButton?.isEnabled = false
            }
        } else if (type=="股利" && subtype=="股票股利"){
            if(stock != "" && distributeShares != 0){
                binding?.doneButton?.isEnabled = true
                binding?.doneButton?.setOnClickListener {
                    addStockDividendRecord()
                    addRecordSuccess()

                }
            } else {
                binding?.doneButton?.isEnabled = false
            }
        }
    }

    private fun addRecordSuccess(){
        finish()
        Utils.showToast(this,"新增成功")
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
                date = "$year$storeMonthOfYear$storeDayOfMonth"

            }, year, month, day

        )
        datePickerDialog.show()
    }

    private fun showToday(){

        var simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
        var todayDate = simpleDateFormat.format(System.currentTimeMillis())
        binding?.dateTextView?.setText(todayDate)


        var simpleDateFormat2 = SimpleDateFormat("yyyyMMdd", Locale.TAIWAN)
        var todayDateForDatabase = simpleDateFormat2.format(System.currentTimeMillis())
        date = todayDateForDatabase

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
            transactionTypeCode = 1
            transactionType = type+subtype
        } else if (type =="現股" && subtype =="賣出"){
            transactionTypeCode = 2
            transactionType = type+subtype
        } else if (type =="股利" && subtype =="現金股利"){
            transactionTypeCode = 3
            transactionType = subtype
        } else if(type =="股利" && subtype =="股票股利"){
            transactionTypeCode = 4
            transactionType = subtype
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
                    binding?.holdingSharesText?.visibility = View.GONE

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
                    binding?.holdingSharesText?.visibility = View.GONE

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
                        binding?.holdingSharesText?.visibility = View.GONE

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
                        binding?.holdingSharesText?.visibility = View.GONE

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
                        binding?.holdingSharesLinearLayout?.visibility = View.VISIBLE

                        binding?.distributeLinearLayout?.visibility = View.GONE

                        binding?.priceTextView?.text = "價格"
                        binding?.sharesTextView?.text = "股數"
                        binding?.distributeSharesTextView?.text = "交易金額"

                        showHoldingShares()

                    } else if (type == "股利") {
                        type = "股利"
                        subtype = "股票股利"
                        transactionType()

                        binding?.amountLinearLayout?.visibility = View.GONE
                        binding?.feeLinearLayout?.visibility = View.GONE
                        binding?.taxLinearLayout?.visibility = View.GONE
                        binding?.distributeLinearLayout?.visibility = View.VISIBLE
                        binding?.holdingSharesText?.visibility = View.GONE

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
        income = 0
        outcome = 0

        binding?.feeEditText?.setText("")
        binding?.amountEditText?.setText("")
        binding?.taxEditText?.setText("")
        binding?.priceEditText?.setText("")
        binding?.sharesEditText?.setText("")
        binding?.distributeSharesEditText?.setText("")
    }


    //編輯滑動畫面
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
                        outcome = amount
                        binding?.amountEditText?.setText(Utils.numberFormat(amount))
                    } else if (type == "現股"&& subtype == "賣出"){
                        amount = (price*shares.toDouble()).toInt()-fee-tax
                        income = amount
                        binding?.amountEditText?.setText(Utils.numberFormat(amount))

                    } else if (type == "股利"&& subtype == "現金股利"){
                        amount = (price*shares.toDouble()).toInt()-fee
                        income = amount
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
                outcome = amount

            } else if (type=="現股" && subtype=="賣出"){

                fee = (value*0.001425*feeDiscount).toInt()

                if(fee < minimumFee){
                    fee = minimumFee
                }

                tax = (value*0.003).toInt()

                amount = (value-fee-tax).toInt()
                income = amount

            } else if (type=="股利" && subtype=="現金股利"){
                fee = 10
                amount =(value - fee).toInt()
                income = amount

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

    //format形式
    fun showDataIntoEdittext(){
        binding?.feeEditText?.setText(Utils.numberFormat(fee))
        binding?.amountEditText?.setText(Utils.numberFormat(amount))
        binding?.taxEditText?.setText(Utils.numberFormat(tax))
        binding?.distributeSharesEditText?.setText(Utils.numberFormat(distributeShares))

        doneButtonClickable()
    }

    //股數的加減按鈕
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

}




