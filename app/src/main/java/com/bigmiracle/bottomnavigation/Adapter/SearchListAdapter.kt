package com.bigmiracle.bottomnavigation.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Model.Stock
import com.bigmiracle.bottomnavigation.R

class SearchListAdapter(
    private val context: Context,
    private val searchStockList: List<Stock>
) : RecyclerView.Adapter<SearchListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView= view.findViewById(R.id.stockAdapterTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchListAdapter.ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_search_list_item,parent,false)
        return ItemViewHolder(adapterLayout)

    }

    override fun onBindViewHolder(holder: SearchListAdapter.ItemViewHolder, position: Int) {
        val item = searchStockList[position]
        holder.textView.text = "${item.stockId} ${item.stockName}"
    }

    override fun getItemCount(): Int {
        return searchStockList.size
    }

}