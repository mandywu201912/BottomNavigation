package com.bigmiracle.bottomnavigation.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.bigmiracle.bottomnavigation.Model.Stock
import com.bigmiracle.bottomnavigation.R

class stockListAdapter(private val mContext: Context,
                       private val viewResourceId: Int,
                       private val items: ArrayList<Stock>) : ArrayAdapter<Stock>(mContext, viewResourceId, items)  {

    private val itemsAll = items.clone() as ArrayList<Stock>
    private var suggestions = ArrayList<Stock>()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View? = convertView
        if (v == null) {
            val vi = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(viewResourceId, null)
        }
        val stock: Stock? = items[position]
        if (stock != null) {
            val stockTextView = v?.findViewById(R.id.stockAdapterTextView) as TextView?
            stockTextView?.text =  "${stock.stockId} ${stock.stockName}"
        }
        return v!!
    }

    override fun getFilter(): Filter {
        return nameFilter
    }

    private var nameFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String {
            return (resultValue as Stock).stockId+" "+(resultValue as Stock).stockName
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                for (stock in itemsAll) {

                    var stockString = "${stock.stockId} ${stock.stockName}"

                    if (stockString.contains(constraint.toString()) || stockString.contains(constraint.toString())) {
                        suggestions.add(stock)
                    }

                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList =  results?.values as ArrayList<Stock>?

            if (results != null && results.count > 0) {
                clear()
                for (c: Stock in filteredList ?: listOf<Stock>()) {
                    add(c)
                }
                notifyDataSetChanged()
            }
        }
    }
}










//class AutoCompleteTextViewAdapter(
//    private val c: Context,
//    @LayoutRes private val layoutResource: Int,
//    private val nameList: Array<NameList>) :
//    ArrayAdapter<NameList>(c, layoutResource, nameList) {
//
//    var filteredNames: List<NameList> = listOf()
//
//    override fun getCount(): Int = filteredNames.size
//
//
//    override fun getItem(position: Int): NameList = filteredNames[position]
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = convertView ?: LayoutInflater.from(c).inflate(layoutResource, null, false)
//        view.tvId.text = filteredNames[position].id.toString()
//        view.tvName.text = filteredNames[position].name
//
//        return view
//    }
//
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
//                @Suppress("UNCHECKED_CAST")
//                filteredNames = filterResults.values as List<NameList>
//
//                notifyDataSetChanged()
//            }
//
//            override fun performFiltering(charSequence: CharSequence?): FilterResults {
//                val queryString = charSequence?.toString()?.toLowerCase()
//
//                val filterResults = FilterResults()
//                filterResults.values =
//                    nameList.filter {
//                        it.name.toLowerCase().contains(queryString.toString()) || it.id.toString().contains(queryString.toString())
//                    }
//                return filterResults
//            }
//        }
//    }