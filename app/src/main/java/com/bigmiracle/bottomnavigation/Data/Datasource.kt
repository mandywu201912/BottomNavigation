package com.bigmiracle.bottomnavigation.Data

import com.bigmiracle.bottomnavigation.Model.Stock

class Datasource {
    fun loadAllStocks(): List<Stock> {
        return listOf<Stock>(
            Stock("2330","台積電"),
            Stock("2303","聯電"),
            Stock("8070","長華"),
            Stock("0050","元大台灣50"),
            Stock("0056","元大高股息"),
            Stock("2603","長榮"),
            Stock("2615","萬海"),
            Stock("2609","陽明"),
            Stock("5201","凱衛"),
            Stock("2002","中鋼")
        )
    }

    fun loadRecentSearchStocks(): List<Stock> {
        return listOf<Stock>(
            Stock("2330","台積電"),
            Stock("2303","聯電"),
            Stock("8070","長華"),
            Stock("0050","元大台灣50"),
            Stock("0056","元大高股息")
        )
    }
}
