package com.bigmiracle.bottomnavigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bigmiracle.bottomnavigation.Database.Record.RecordDao
import com.bigmiracle.bottomnavigation.Database.Record.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordViewModel(private val recordDao: RecordDao): ViewModel() {
    fun fullRecord(): Flow<List<RecordEntity>> = recordDao.fetchAllRecord()
    fun recordForStockId(id: Int): Flow<List<RecordEntity>> = recordDao.fetchRecordByStockId(id)
}

class RecordViewModelFactory(
    private val recordDao: RecordDao
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecordViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(recordDao) as T
        }

        throw IllegalAccessException("UnKnown ViewModel class")
    }
}