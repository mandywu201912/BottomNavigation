package com.bigmiracle.bottomnavigation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.databinding.AdapterStockDetailItemBinding

class StockDetailAdapter(
    private val onItemClicked: (HoldingEntity) -> Unit
) : ListAdapter<HoldingEntity, StockDetailAdapter.HoldingViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<HoldingEntity>(){
            override fun areItemsTheSame(oldItem: HoldingEntity, newItem: HoldingEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HoldingEntity, newItem: HoldingEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {

        return HoldingViewHolder(
            AdapterStockDetailItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {

        var current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }




    class HoldingViewHolder(
        private var binding: AdapterStockDetailItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(holdingEntity: HoldingEntity){
            binding.apply {
                binding?.dateTextView.text = holdingEntity.date
                binding?.stockPriceTextView.text = holdingEntity.price.toString()
                binding?.stockSharesTextView.text = holdingEntity.share.toString()
            }
        }
    }
}

