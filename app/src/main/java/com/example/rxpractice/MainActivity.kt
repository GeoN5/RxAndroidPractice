package com.example.rxpractice

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.rxpractice.model.MarketAll
import com.example.rxpractice.model.Ticker
import com.example.rxpractice.network.provideMyApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val api by lazy { provideMyApi() }
    private lateinit var disposable: Disposable
    private lateinit var disposable2: Disposable
    private var krwList: ArrayList<String> = ArrayList()
    private var tickerList: ArrayList<Ticker> = ArrayList()
    private lateinit var recyclerAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callFlatMap()
        //callZip()
    }

    //zip을 이용한 호출 후 결합
    private fun callZip() {
        val marketOb = api.getMarket()
    /*getTicker함수의 인자로 getMarket()호출 후 받아온 response를 callFlatMap()에서 처럼 가공하여 넘겨야 하는데
      여기서 zip을 쓸때는 어떻게 가져와서 넘겨야하는지에 대한 방법을 모르겠습니다.
      따라서 일단은 KRW로 시작하는 값들을 넣었습니다.
    */
        val tickerOb = api.getTicker(
            "KRW-BTC, KRW-DASH, KRW-ETH, KRW-NEO, KRW-MTL, KRW-LTC," +
                    " KRW-STRAT, KRW-XRP, KRW-ETC, KRW-OMG, KRW-SNT, KRW-WAVES, KRW-PIVX, KRW-XEM, KRW-ZEC, KRW-XMR, KRW-QTUM, KRW-GNT," +
                    " KRW-LSK, KRW-STEEM, KRW-XLM, KRW-ARDR, KRW-KMD, KRW-ARK, KRW-STORJ, KRW-GRS, KRW-VTC, KRW-REP, KRW-EMC2, KRW-ADA, KRW-SBD," +
                    " KRW-POWR, KRW-MER, KRW-BTG, KRW-ICX, KRW-EOS, KRW-STORM, KRW-TRX, KRW-MCO, KRW-SC, KRW-GTO, KRW-IGNIS, KRW-ONT, KRW-DCR," +
                    " KRW-ZIL, KRW-POLY, KRW-ZRX, KRW-SRN, KRW-LOOM, KRW-BCH, KRW-ADT, KRW-ADX, KRW-BAT, KRW-IOST, KRW-DMT, KRW-RFR, KRW-CVC," +
                    " KRW-IQ, KRW-IOTA, KRW-OST, KRW-MFT, KRW-ONG, KRW-GAS, KRW-MEDX, KRW-UPP, KRW-ELF, KRW-KNC, KRW-BSV, KRW-THETA, KRW-WAX," +
                    " KRW-EDR, KRW-QKC, KRW-CPT, KRW-BTT, KRW-MOC, KRW-COSM, KRW-ENJ, KRW-TFUEL, KRW-MANA, KRW-ANKR, KRW-NPXS, KRW-TTC, KRW-AERGO, KRW-ATOM, KRW-TT"
        )
        val unitOb = Observable.zip(marketOb, tickerOb,
            BiFunction<List<MarketAll>, List<Ticker>, Unit> { krwList, tickerList ->
                //원래라면 해당 스코프에서는 이 로직은 의미가 없는 것 같습니다.(단순 동작을 위한 추가)
                krwList.forEach {
                    if (it.market.substring(0, 3) == "KRW") {
                        this.krwList.add(it.market)
                    }
                }
                this.tickerList.addAll(tickerList)
            }
        )
        disposable = unitOb.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setRecyclerView()
            }) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

    }


    //순서대로 처리하기
    private fun callFlatMap() {
        val observable = api.getMarket()
        disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                list.forEach {
                    if (it.market.substring(0, 3) == "KRW") {
                        krwList.add(it.market)
                    }
                }
            }) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

        disposable2 = observable.flatMap { api.getTicker(krwList.joinToString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tickerList.addAll(it)
                setRecyclerView()
            }) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun setRecyclerView() {
        recyclerAdapter = RecyclerViewAdapter(krwList, tickerList, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        disposable2.dispose()
    }

}
