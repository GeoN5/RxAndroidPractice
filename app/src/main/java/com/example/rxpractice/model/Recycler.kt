package com.example.rxpractice.model

//not used
data class Recycler(
    val market: String,//코인명
    val tradePrice: Double,//현재가
    val signedChangeRate: Double, //전일대비
    val accTradePrice24h: Double //거래대금
)