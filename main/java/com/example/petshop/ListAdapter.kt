package com.example.petshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(context: Context, dataArrayList:ArrayList<ListData?>?):
ArrayAdapter<ListData?>(context, R.layout.dog_item, dataArrayList!!){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        val listData = getItem(position)

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.dog_item, parent, false)
        }

        val dogImage = view!!.findViewById<ImageView>(R.id.dogImage)
        val itemName = view.findViewById<TextView>(R.id.dogItemName)
        val itemPrice = view.findViewById<TextView>(R.id.dogItemPrice)

        dogImage.setImageResource(listData!!.image)
        itemName.text = listData.name
        itemPrice.text = listData.price

        return view

    }
}