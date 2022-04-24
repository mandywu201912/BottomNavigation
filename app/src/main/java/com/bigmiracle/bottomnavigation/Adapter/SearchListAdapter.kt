package com.bigmiracle.bottomnavigation.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Model.Stock
import com.bigmiracle.bottomnavigation.databinding.AdapterSearchListItemBinding

class SearchListAdapter(
    private val context: Context,
    private val searchStockList: List<Stock>
) : RecyclerView.Adapter<SearchListAdapter.ItemViewHolder>() {

    class ItemViewHolder(binding: AdapterSearchListItemBinding) : RecyclerView.ViewHolder(binding.root){
        val textView = binding.stockAdapterTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemViewHolder {

        return ItemViewHolder(AdapterSearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: SearchListAdapter.ItemViewHolder, position: Int) {
        val item = searchStockList[position]
        holder.textView.text = "${item.stockId} ${item.stockName}"
    }

    override fun getItemCount(): Int {
        return searchStockList.size
    }

}