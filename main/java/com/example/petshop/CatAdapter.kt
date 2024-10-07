package com.example.petshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CatAdapter(context: Context, dataArrayList:ArrayList<ListData?>?):
    ArrayAdapter<ListData?>(context, R.layout.cat_item, dataArrayList!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        val listData = getItem(position)

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.cat_item, parent, false)
        }

        val catImage = view!!.findViewById<ImageView>(R.id.catImage)
        val itemName = view.findViewById<TextView>(R.id.catItemName)
        val itemPrice = view.findViewById<TextView>(R.id.catItemPrice)

        catImage.setImageResource(listData!!.image)
        itemName.text = listData.name
        itemPrice.text = listData.price

        return view

    }
}