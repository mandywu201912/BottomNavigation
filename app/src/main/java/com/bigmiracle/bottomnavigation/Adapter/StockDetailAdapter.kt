package com.bigmiracle.bottomnavigation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.Utils
import com.bigmiracle.bottomnavigation.databinding.AdapterStockDetailItemBinding


class StockDetailAdapter(
    currentPrice: Double,
//    private val onItemClicked: (HoldingEntity) -> Unit

) : ListAdapter<HoldingEntity, StockDetailAdapter.HoldingViewHolder>(DiffCallback) {




    var price = currentPrice
    var profit:Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {

        return HoldingViewHolder(
            AdapterStockDetailItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {

        var current = getItem(position)
//        holder.itemView.setOnClickListener {
//            onItemClicked(current)
//        }


        holder.bind(current,price)



    }


    class HoldingViewHolder(private var binding: AdapterStockDetailItemBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(holdingEntity: HoldingEntity,currentPrice: Double){

            var buyPrice: Double = holdingEntity.price
            var buyShares: Double = holdingEntity.share.toDouble()
            var cost: Int = holdingEntity.outcome+ holdingEntity.fee
            var nowValue: Int = (currentPrice*buyShares).toInt()
            var profit: Int = nowValue-cost
            var profitRatio: Double =(profit.toDouble()/cost.toDouble())*100

            binding.apply {
                binding?.dateTextView.text = holdingEntity.date
                binding?.stockProfitTextView.text = profit.toString()
                binding?.stockProfitRatioTextView.text = "${Utils.twoDigitDecimalFormat(profitRatio)}%"
                binding?.stockPriceTextView.text = buyPrice.toString()
                binding?.stockSharesTextView.text = buyShares.toString()

            }
        }
    }

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

}

