package com.bigmiracle.bottomnavigation.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Insert
    suspend fun insertRecord(recordEntity: RecordEntity)

    @Insert
    suspend fun insertHolding(HoldingEntity: HoldingEntity)

    @Insert
    suspend fun insertClosed(ClosedEntity: ClosedEntity)




    @Update
    suspend fun updateRecord(recordEntity: RecordEntity)

    @Update
    suspend fun updateHolding(holdingEntity: HoldingEntity)

    @Update
    suspend fun updateClosed(closedEntity: ClosedEntity)




    @Delete
    suspend fun deleteRecord(recordEntity: RecordEntity)

    @Delete
    suspend fun deleteHolding(HoldingEntity: HoldingEntity)

    @Delete
    suspend fun deleteClosed(ClosedEntity: ClosedEntity)



    //取所有的資料
    @Query("SELECT * FROM `record-table`ORDER BY 交易日期 DESC")
    fun getAllRecord(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM `holding-table`ORDER BY 交易日期 DESC")
    fun getAllHolding(): Flow<List<HoldingEntity>>

    @Query("SELECT * FROM `closed-table`ORDER BY 交易日期 DESC")
    fun getAllClosed(): Flow<List<ClosedEntity>>



    //取特定的資料
    @Query("SELECT * FROM `record-table` where 股票代號 = :id")
    fun getRecordByStockId(id: String): Flow<List<RecordEntity>>

    @Query("SELECT * FROM `holding-table` where 股票代號 = :id")
    fun getHoldingByStockId(id: String): Flow<List<HoldingEntity>>

    @Query("SELECT * FROM `closed-table` where 股票代號 = :id")
    fun getClosedByStockId(id: String): Flow<List<ClosedEntity>>

}
