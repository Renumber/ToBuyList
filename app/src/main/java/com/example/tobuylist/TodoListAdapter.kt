package com.example.tobuylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.DecimalFormat

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>) : RealmBaseAdapter<Todo>(realmResult) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh : ViewHolder
        val view : View

        if(convertView == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)

            vh = ViewHolder(view)
            view.tag = vh
        } else{
            view = convertView
            vh = view.tag as ViewHolder
        }
        if(adapterData != null){
            val item = adapterData!![position]
            vh.nameTextView.text = item.name
            val priceFormatter = DecimalFormat("###,###")
            vh.priceTextView.text = priceFormatter.format(item.price.toString().toInt()) + "원"
            vh.impTextView.text = item.imp.toString()
        }
        return view
    }

    override fun getItemId(position: Int): Long {
        if(adapterData != null){
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }
    class ViewHolder(view: View){
        val nameTextView: TextView = view.findViewById(R.id.name)
        val priceTextView: TextView = view.findViewById(R.id.price)
        val impTextView : TextView = view.findViewById(R.id.imp)
    }

}