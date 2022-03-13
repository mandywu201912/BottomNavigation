package com.bigmiracle.bottomnavigation.Database.Record

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record-table")
data class RecordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @NonNull
    val stockId: String = "",

    @NonNull
    val stockName: String = "",

    @NonNull
    val transactionType: Int = 0,

//    val date: Int,

    val price: Double = 0.0,
    val share: Int = 0,
    val fee: Int = 0,
    val tax: Int = 0,
    val distributionShares: Int = 0,
    val amount: Int = 0

)