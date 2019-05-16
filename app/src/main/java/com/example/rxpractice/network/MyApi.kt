package com.example.rxpractice.network

import com.example.rxpractice.model.MarketAll
import com.example.rxpractice.model.Ticker
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {

    //시세 종목 조회 - 마켓 코드 조회
    @GET("market/all")
    fun getMarket(): Observable<List<MarketAll>>

    //시세 Ticker 조회 - 현재가 정보
    @GET("ticker")
    fun getTicker(@Query("markets") markets: String): Observable<List<Ticker>>


}