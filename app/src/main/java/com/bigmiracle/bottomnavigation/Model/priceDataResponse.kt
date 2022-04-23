package com.bigmiracle.testapi.model2

import com.bigmiracle.bottomnavigation.Model.priceData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class priceDataResponse(
    @SerializedName("data")
    var priceData: List<priceData>
): Serializable