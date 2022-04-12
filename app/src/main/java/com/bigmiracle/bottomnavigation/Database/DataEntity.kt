package com.bigmiracle.bottomnavigation.Database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "record-table")
data class RecordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "交易日期")
    val date: String,

    @NonNull @ColumnInfo(name = "類型代號")
    val transactionTypeCode: Int = 0,

    @NonNull @ColumnInfo(name = "交易類型")
    val transactionType: String = "",

    @NonNull @ColumnInfo(name = "股票代號")
    val stockId: String = "",

    @NonNull @ColumnInfo(name = "股票名稱")
    val stockName: String = "",

    @ColumnInfo(name = "價格")
    val price: Double = 0.0,

    @ColumnInfo(name = "股數")
    val share: Int = 0,

    @ColumnInfo(name = "手續費")
    val fee: Int = 0,

    @ColumnInfo(name = "交易稅")
    val tax: Int = 0,

    @ColumnInfo(name = "支出")
    val outcome: Int = 0,

    @ColumnInfo(name = "收入")
    val income: Int = 0,

    @ColumnInfo(name = "配發股數")
    val distributionShares: Int = 0,

    )

@Entity(tableName = "closed-table")
data class ClosedEntity(

    //現股賣出
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "交易日期")
    val date: String,

    @NonNull @ColumnInfo(name = "類型代號")
    val transactionTypeCode: Int = 0,

    @NonNull @ColumnInfo(name = "交易類型")
    val transactionType: String = "",

    @NonNull @ColumnInfo(name = "股票代號")
    val stockId: String = "",

    @NonNull @ColumnInfo(name = "股票名稱")
    val stockName: String = "",

    @ColumnInfo(name = "買均價")
    val buyAveragePrice: Double = 0.0,

    @ColumnInfo(name = "股數")
    val share: Int = 0,

    @ColumnInfo(name = "買進手續費")
    val buyFee: Int = 0,

    @ColumnInfo(name = "支出")
    val outcome: Int = 0,

    @ColumnInfo(name = "賣價")
    val sellPrice: Double = 0.0,

    @ColumnInfo(name = "賣出手續費")
    val sellFee: Int = 0,

    @ColumnInfo(name = "交易稅")
    val tax: Int = 0,

    @ColumnInfo(name = "收入")
    val income: Int = 0,

    @ColumnInfo(name = "已實現損益")
    val profit: Int = 0,

    @ColumnInfo(name = "已實現損益率")
    val profitRatio: Double = 0.0,



    )

@Entity(tableName = "holding-table")
data class HoldingEntity(

    //現股買入&&股票股利
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "交易日期")
    val date: String,

    @NonNull @ColumnInfo(name = "類型代號")
    val transactionTypeCode: Int = 0,

    @NonNull @ColumnInfo(name = "交易類型")
    val transactionType: String = "",

    @NonNull @ColumnInfo(name = "股票代號")
    val stockId: String = "",

    @NonNull @ColumnInfo(name = "股票名稱")
    val stockName: String = "",

    @ColumnInfo(name = "價格")
    val price: Double = 0.0,

    @ColumnInfo(name = "股數")
    val share: Int = 0,

    @ColumnInfo(name = "手續費")
    val fee: Int = 0,

    @ColumnInfo(name = "支出")
    val outcome: Int = 0,

    @ColumnInfo(name = "均價")
    val averagePrice: Double = 0.0,
)

fun RecordEntity.getFormattedOutcome(): String = NumberFormat.getCurrencyInstance().format(outcome)
