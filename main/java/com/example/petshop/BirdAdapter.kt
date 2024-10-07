package com.example.petshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class BirdAdapter(context: Context, dataArrayList:ArrayList<ListData?>?):
    ArrayAdapter<ListData?>(context, R.layout.bird_item, dataArrayList!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        val listData = getItem(position)

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.bird_item, parent, false)
        }

        val birdImage = view!!.findViewById<ImageView>(R.id.birdImage)
        val itemName = view.findViewById<TextView>(R.id.birdItemName)
        val itemPrice = view.findViewById<TextView>(R.id.birdItemPrice)

        birdImage.setImageResource(listData!!.image)
        itemName.text = listData.name
        itemPrice.text = listData.price

        return view

    }
}