package com.bigmiracle.bottomnavigation.Database.Record

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(recordEntity: RecordEntity)

    @Update
    suspend fun update(recordEntity: RecordEntity)

    @Delete
    suspend fun delete(recordEntity: RecordEntity)

    @Query("SELECT * FROM `record-table` ORDER BY ")
    fun fetchAllRecord(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM `record-table` where stockId = :id")
    fun fetchRecordByStockId(id: Int): Flow<List<RecordEntity>>

}