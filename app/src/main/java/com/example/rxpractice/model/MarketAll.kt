package com.example.rxpractice.model

import com.google.gson.annotations.SerializedName

data class MarketAll(
    @SerializedName("market")
    val market: String, //Ticker 조회 파라미터
    @SerializedName("korean_name")
    val koreanName: String,
    @SerializedName("english_name")
    val englishName: String
)