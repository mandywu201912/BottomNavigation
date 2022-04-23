package com.bigmiracle.bottomnavigation.Network

import com.bigmiracle.testapi.model2.priceDataResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface priceDataService {

    @GET("/v2/real-time-quotes/data")
    fun getData(
        @Query("stock_id") stockId: String,
        ): Call<priceDataResponse>


}