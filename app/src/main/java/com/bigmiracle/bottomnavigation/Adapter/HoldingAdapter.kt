package com.bigmiracle.bottomnavigation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.Model.priceData
import com.bigmiracle.bottomnavigation.Utils
import com.bigmiracle.bottomnavigation.databinding.AdapterHoldingItemBinding

class HoldingAdapter(
    private val priceDataList: List<priceData>
) : ListAdapter<HoldingEntity, HoldingAdapter.HoldingViewHolder>(DiffCallback) {

    private var onClickListener: OnClickListener? = null

    class HoldingViewHolder(private var binding: AdapterHoldingItemBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(holdingEntity: HoldingEntity,price: priceData){
            binding.apply {

                var buyPrice: Double = holdingEntity.price
                var nowPrice: Double = price.nowPrice.toDouble()
                var share: Int = holdingEntity.share
                var profit: Int = ((nowPrice-buyPrice)*share).toInt()
                var profitRatio: Double = (profit/(buyPrice*share))*100


                binding?.stockIdTextView.text = holdingEntity.stockId
                binding?.stockNameTextView.text = holdingEntity.stockName
                binding?.buyPriceTextView.text = Utils.twoDigitDecimalFormat(buyPrice)
                binding?.stockSharesTextView.text = holdingEntity.share.toString()
                binding?.stockPriceTextView.text = Utils.twoDigitDecimalFormat(nowPrice)
                binding?.profitTextView.text = profit.toString()
                binding?.profitRatioTextView.text = "${Utils.twoDigitDecimalFormat(profitRatio)}%"
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {

        return HoldingViewHolder(AdapterHoldingItemBinding.inflate(LayoutInflater.from(
            parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        val model = getItem(position)
        val price = priceDataList[position]
        holder.bind(model,price)

        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener!!.onClick(position,model)
            }
        }

    }


    interface OnClickListener{
        fun onClick(position: Int, model: HoldingEntity)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
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


//class HoldingAdapter(
//    private val onItemClicked: (HoldingEntity) -> Unit
//) : ListAdapter<HoldingEntity, HoldingAdapter.HoldingViewHolder>(DiffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
//
//        return HoldingViewHolder(
//            AdapterHoldingItemBinding.inflate(
//                LayoutInflater.from(
//                    parent.context
//                ),parent,false
//            )
//        )
//
////        val viewHolder = HoldingViewHolder(
////            AdapterHoldingItemBinding.inflate(
////                LayoutInflater.from(parent.context),
////                parent,
////                false
////            )
////        )
////
////        viewHolder.itemView.setOnClickListener {
////            val position = viewHolder.adapterPosition
////            onItemClicked(getItem(position))
////        }
////
////        return viewHolder
//    }
//
//    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
//        val current = getItem(position)
//        holder.itemView.setOnClickListener {
//            onItemClicked(current)
//        }
//
//        holder.bind(current)
//    }
//
//
//
//
//    class HoldingViewHolder(
//        private var binding: AdapterHoldingItemBinding
//    ): RecyclerView.ViewHolder(binding.root){
//
//        fun bind(holdingEntity: HoldingEntity){
//            binding.apply {
//                binding?.stockIdTextView.text = holdingEntity.stockId
//                binding?.stockNameTextView.text = holdingEntity.stockName
//                binding?.stockPriceTextView.text = holdingEntity.price.toString()
//                binding?.stockSharesTextView.text = holdingEntity.share.toString()
//            }
//        }
//    }
//
//
//    companion object {
//        private val DiffCallback = object : DiffUtil.ItemCallback<HoldingEntity>(){
//            override fun areItemsTheSame(oldItem: HoldingEntity, newItem: HoldingEntity): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: HoldingEntity, newItem: HoldingEntity): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}
//
//
