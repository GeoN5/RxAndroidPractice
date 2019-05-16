package com.example.rxpractice.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun provideMyApi(): MyApi = Retrofit.Builder()
    .baseUrl("https://api.upbit.com/v1/")
    // 네트워크 요청 로그를 표시해 줍니다.
    .client(provideOkHttpClient(provideLoggingInterceptor()))
    // 받은 응답을 옵저버블 형태로 변환해 줍니다.
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(MyApi::class.java)


// 네트뭐크 통신에 사용할 클라이언트 객체를 생성합니다.
private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    val b = OkHttpClient.Builder()
    // 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
    b.addInterceptor(interceptor)
    return b.build()
}

// 네트워크 요청/응답을 로그에 표시하는 Interceptor 객체를 생성합니다.
private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return interceptor
}