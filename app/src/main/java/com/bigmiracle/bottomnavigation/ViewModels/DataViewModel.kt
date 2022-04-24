package com.bigmiracle.bottomnavigation.ViewModels

import androidx.lifecycle.*
import com.bigmiracle.bottomnavigation.Database.ClosedEntity
import com.bigmiracle.bottomnavigation.Database.DataDao
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.Database.RecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DataViewModel(private val dataDao: DataDao): ViewModel() {


    val allRecords: LiveData<List<RecordEntity>> = dataDao.getAllRecord().asLiveData()


    fun getHoldingForSell(id: String,date: String):Flow<List<HoldingEntity>> {
        return dataDao.getHoldingForSell(id, date)
    }


    fun getSumSharesByStockId(stockId: String): Flow<Int> {
        return dataDao.getSumSharesByStockId(stockId)
    }

    suspend fun getDistinctStockId():List<String>{
        return dataDao.loadDistinctStockId()
    }




    // 新增Record
    fun addNewRecord(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, price: Double, shares: Int, fee: Int, tax: Int, outcome: Int, income: Int, distributionShares: Int, date: String){

        val newRecord = getNewRecordEntry(
            stockId, stockName, transactionTypeCode,transactionType,price,shares,fee,tax,outcome,income,distributionShares,date)
        insertRecord(newRecord)

    }

    private fun insertRecord(record: RecordEntity){
        viewModelScope.launch {
            dataDao.insertRecord(record)
        }
    }

    private fun getNewRecordEntry(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, price: Double, shares: Int, fee: Int, tax: Int, outcome: Int, income: Int, distributionShares: Int, date: String): RecordEntity {
        return RecordEntity(
            stockId = stockId,
            stockName = stockName,
            transactionType = transactionType,
            transactionTypeCode = transactionTypeCode,
            price = price,
            share = shares,
            fee = fee,
            tax = tax,
            outcome = outcome,
            income = income,
            distributionShares = distributionShares,
            date = date

        )
    }



    // 新增Holding
    fun addNewHolding(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, price: Double, shares: Int, fee: Int, outcome: Int, averagePrice: Double , date: String){

        val newHolding = getNewHoldingEntry(
            stockId, stockName, transactionTypeCode,transactionType,price,shares,fee,outcome,averagePrice,date)
        insertHolding(newHolding)

    }

    private fun insertHolding(holding: HoldingEntity){
        viewModelScope.launch {
            dataDao.insertHolding(holding)
        }
    }

    private fun getNewHoldingEntry(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, price: Double, shares: Int, fee: Int, outcome: Int, averagePrice: Double, date: String): HoldingEntity {
        return HoldingEntity(
            stockId = stockId,
            stockName = stockName,
            transactionType = transactionType,
            transactionTypeCode = transactionTypeCode,
            price = price,
            share = shares,
            fee = fee,
            outcome = outcome,
            date = date,
            averagePrice =  averagePrice
        )
    }


    // 新增Holding
    fun addNewClosed(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, buyAveragePrice: Double, shares: Int, buyFee: Int, outcome: Int, sellPrice: Double ,sellFee: Int, tax: Int,income: Int,profit: Int, profitRatio: Double, date: String){

        val newClosed = getNewClosedEntry(
            stockId, stockName, transactionTypeCode,transactionType,buyAveragePrice,shares,buyFee,outcome,sellPrice,sellFee,tax,income,profit,profitRatio,date)
        insertClosed(newClosed)

    }

    private fun insertClosed(closed: ClosedEntity){
        viewModelScope.launch {
            dataDao.insertClosed(closed)
        }
    }

    private fun getNewClosedEntry(stockId: String, stockName: String, transactionTypeCode: Int, transactionType: String, buyAveragePrice: Double, shares: Int, buyFee: Int, outcome: Int, sellPrice: Double, sellFee: Int, tax: Int, income: Int, profit: Int, profitRatio: Double, date: String): ClosedEntity {
        return ClosedEntity(
            stockId = stockId,
            stockName = stockName,
            transactionType = transactionType,
            transactionTypeCode = transactionTypeCode,
            buyAveragePrice = buyAveragePrice,
            share = shares,
            buyFee = buyFee,
            outcome = outcome,
            sellPrice =  sellPrice,
            sellFee = sellFee,
            tax = tax,
            income = income,
            profit = profit,
            profitRatio = profitRatio,
            date = date
        )
    }


    //現股買進欄位狀態
    fun isBuyEntryValid(stockId: String,stockName: String, price: Double ,shares: Int, fee: Int,outcome: Int): Boolean {

        if(stockId.isBlank() || stockName.isBlank() || price == 0.0 || shares == 0 || fee == 0 || outcome == 0){
            return false
        }
        return true
    }


    //現股賣出欄位狀態
    fun isSellEntryValid(stockId: String, stockName: String, price: Double ,shares: Int, fee: Int, tax: Int,income: Int): Boolean {

        if(stockId.isBlank() || stockName.isBlank() || price == 0.0 || shares == 0 || fee == 0 || income == 0 || tax == 0){
            return false
        }
        return true
    }

    //現金股利欄位狀態
    fun isCashDividendEntryValid(stockId: String,stockName: String, income: Int): Boolean {

        if(stockId.isBlank() || stockName.isBlank() || income == 0){
            return false
        }
        return true
    }

    //股票股利欄位狀態
    fun isStockDividendEntryValid(stockId: String,stockName: String,distributionShares: Int): Boolean {

        if(stockId.isBlank() || stockName.isBlank() || distributionShares == 0){
            return false
        }
        return true
    }





//    fun getSumSharesByStockId(stockId: String): Flow<Int> {
//        return dataDao.getSumSharesByStockId(stockId)
//    }





    //這檔股票有庫存才能現股賣出
    //且庫存的數量要足夠
    //找到全部的2330庫存，把股數相加

//    fun isSellAvailable(stockId: String, shares: Int){
//        var stockList = getHolding(stockId)
//        for(holding in stockList){
//            holding.
//
//        }
//
//
//    }



//    fun fullRecord(): Flow<List<RecordEntity>> = dataDao.getAllRecord()

//    fun getHolding(id: String): LiveData<List<HoldingEntity>> = dataDao.getHoldingByStockId(id).asLiveData()
    fun getHolding(id: String): Flow<List<HoldingEntity>> = dataDao.getHoldingByStockId(id)


//    fun getData(id: String) : Flow<List<HoldingEntity>> = dataDao.fetchByStockId(id)

    fun recordForStockId(id: String): Flow<List<RecordEntity>> = dataDao.getRecordByStockId(id)

    fun fullHolding(): Flow<List<HoldingEntity>> = dataDao.getAllHolding()
    fun holdingForStockId(id: String): Flow<List<HoldingEntity>> = dataDao.getHoldingByStockId(id)


    fun stockHolding(id: String): LiveData<List<HoldingEntity>>{
        return dataDao.getHoldingByStockId(id).asLiveData()
    }

    fun fullClosed(): Flow<List<ClosedEntity>> = dataDao.getAllClosed()
    fun closedForStockId(id: String): Flow<List<ClosedEntity>> = dataDao.getClosedByStockId(id)

}

class DataViewModelFactory(private val dataDao: DataDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DataViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DataViewModel(dataDao) as T
        }

        throw IllegalAccessException("UnKnown ViewModel class")
    }
}