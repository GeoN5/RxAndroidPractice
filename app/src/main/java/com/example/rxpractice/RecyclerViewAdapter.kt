package com.example.rxpractice

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rxpractice.model.Ticker
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_coin.view.*

class RecyclerViewAdapter(
    private val krwList: ArrayList<String>,
    private val tickerList: ArrayList<Ticker>,
    private val context: Context
) : RecyclerView.Adapter<CoinViewHolder>() {

    //private val tickerList: ArrayList<Ticker> = ArrayList()

//    fun setList(tickerList: ArrayList<Ticker>) {
//        this.tickerList.clear()
//        this.tickerList.addAll(tickerList)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CoinViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_coin, parent, false))

    override fun getItemCount() = tickerList.size

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(krwList[position], tickerList[position])
    }

}

@SuppressLint("SetTextI18n")
class CoinViewHolder(itemView: View) : AndroidExtensionsViewHolder(itemView) {

    fun bind(krw: String, ticker: Ticker) {
        with(containerView) {
            coinNameText.text = krw

            currentPriceText.text = ticker.tradePrice.toString()

            dayBeforeText.text = if ((ticker.signedChangeRate * 100).toString().length > 4) {
                "${(ticker.signedChangeRate * 100).toString().substring(
                    0,
                    ticker.signedChangeRate.toString().lastIndexOf(".") + 3
                )}%"
            } else {
                "${(ticker.signedChangeRate * 100)}%"
            }

            if (dayBeforeText.text.toString().substring(0, dayBeforeText.text.toString().length - 1).toDouble() > 0) {
                dayBeforeText.setTextColor(Color.RED)
            } else {
                dayBeforeText.setTextColor(Color.BLUE)
            }

            transactionPriceText.text = "${ticker.accTradePrice24h.toInt()}M"
        }
    }

}

abstract class AndroidExtensionsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer