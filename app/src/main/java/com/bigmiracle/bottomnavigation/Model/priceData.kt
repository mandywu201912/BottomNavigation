package com.bigmiracle.bottomnavigation.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class priceData(
    @SerializedName("股票代號")
    var stockId: String,

    @SerializedName("股票名稱")
    var stockName: String,

    @SerializedName("開盤價")
    var openPrice: String,

    @SerializedName("最高價")
    var highPrice: String,

    @SerializedName("最低價")
    var lowPrice: String,

    @SerializedName("當盤成交價")
    var nowPrice: String,

    @SerializedName("參考價")
    var referencePrice: String,

    @SerializedName("漲跌")
    var upDown: String,

    @SerializedName("漲跌幅")
    var upDownRatio: String,

    @SerializedName("當盤成交量")
    var nowVolume: String,

    @SerializedName("成交金額")
    var dealAmount: String,

    @SerializedName("累積成交量")
    var allVolume: String,

    @SerializedName("漲停價")
    var highestPrice: String,

    @SerializedName("跌停價")
    var lowestPrice: String,

    @SerializedName("最近交易日期")
    var date: String,

    @SerializedName("最近成交時刻")
    var time: String
) : Serializable