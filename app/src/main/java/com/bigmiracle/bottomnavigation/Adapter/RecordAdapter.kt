package com.bigmiracle.bottomnavigation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Database.RecordEntity
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.Utils
import com.bigmiracle.bottomnavigation.databinding.AdapterRecordItemBinding

class RecordAdapter(
    private val onItemClicked: (RecordEntity) -> Unit
) : ListAdapter<RecordEntity, RecordAdapter.RecordViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RecordEntity>(){
            override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val viewHolder = RecordViewHolder(
            AdapterRecordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

//        viewHolder.itemView.setOnClickListener {
//            val position = viewHolder.adapterPosition
//            onItemClicked(getItem(position))
//        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }




    class RecordViewHolder(
        private var binding: AdapterRecordItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(recordEntity: RecordEntity){
            when(recordEntity.transactionTypeCode){
                1-> binding.typeImage.setImageResource(R.drawable.type_stock_buy)
                2-> binding.typeImage.setImageResource(R.drawable.type_stock_sell)
                3-> binding.typeImage.setImageResource(R.drawable.type_dividend_cash)
                4-> binding.typeImage.setImageResource(R.drawable.type_dividend_stock)
            }

            when(recordEntity.transactionTypeCode){
                1-> binding?.stockAmountTextView.text = Utils.numberFormat(recordEntity.outcome)
                2-> binding?.stockAmountTextView.text = Utils.numberFormat(recordEntity.income)
                3-> binding?.stockAmountTextView.text = Utils.numberFormat(recordEntity.income)
            }

            binding?.stockIdTextView.text = recordEntity.stockId
            binding?.stockNameTextView.text = recordEntity.stockName
            binding?.stockPriceTextView.text = recordEntity.price.toString()
            binding?.stockSharesTextView.text = recordEntity.share.toString()
//            binding?.stockAmountTextView.text = Utils.numberFormat(recordEntity.amount)
            binding?.dateTextView.text = recordEntity.date
        }
    }
}

