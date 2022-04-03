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

    //這檔股票有庫存才能現股賣出
    //且庫存的數量要足夠
    //找到全部的2330庫存，把股數相加
    fun isStockHoldingAvailable(holding: HoldingEntity):Boolean{
        return (holding.share > 0)
    }

    //現股買進欄位狀態
    private fun isType1EntryValid(stockId: String,stockName: String, price: Double ,shares: Int, fee: Int,outcome: Int): Boolean {

        if(stockId.isBlank() || stockName.isBlank() || price == 0.0 || shares == 0 || fee == 0 || outcome == 0){
            return false
        }
        return true
    }








    fun fullRecord(): Flow<List<RecordEntity>> = dataDao.getAllRecord()

//    fun getData(id: String) : Flow<List<HoldingEntity>> = dataDao.fetchByStockId(id)

    fun recordForStockId(id: String): Flow<List<RecordEntity>> = dataDao.getRecordByStockId(id)

    fun fullHolding(): Flow<List<HoldingEntity>> = dataDao.getAllHolding()
    fun holdingForStockId(id: String): Flow<List<HoldingEntity>> = dataDao.getHoldingByStockId(id)

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