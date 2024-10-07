package com.example.petshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(
    private val itemList: ArrayList<ItemData>,
    private val onQuantityChangeListener: OnQuantityChangeListener
) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodImage:ImageView  = itemView.findViewById(R.id.foodImage)
        val foodTitle: TextView = itemView.findViewById(R.id.title)
        val feeEachItem: TextView = itemView.findViewById(R.id.feeEachItem)
        val totalEachItem: TextView = itemView.findViewById(R.id.totalEachItem)
        val quantity: TextView = itemView.findViewById(R.id.numberItem)
        val plusCartBtn: TextView = itemView.findViewById(R.id.plusCartBtn)
        val minusCartBtn: TextView = itemView.findViewById(R.id.minusCartBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cart, parent, false)
        return  ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return  itemList.size

    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
       val currentItem = itemList[position]

        holder.foodImage.setImageResource(currentItem.Image)
        holder.foodTitle.text = currentItem.title
        holder.feeEachItem.text = currentItem.feeEachItem.toString()
        holder.totalEachItem.text = currentItem.totalEachItem.toString()
        holder.quantity.text = currentItem.quantity.toString()

        holder.plusCartBtn.setOnClickListener{
            currentItem.quantity += 1
            currentItem.totalEachItem = currentItem.quantity * currentItem.feeEachItem
            notifyItemChanged(position)

            onQuantityChangeListener.onQuantityChanged()
        }

        holder.minusCartBtn.setOnClickListener{
            if (currentItem.quantity > 0){
                currentItem.quantity -= 1
                currentItem.totalEachItem = currentItem.quantity * currentItem.feeEachItem
                notifyItemChanged(position)

                onQuantityChangeListener.onQuantityChanged()
            }
        }


    }

    interface OnQuantityChangeListener{
        fun onQuantityChanged()
    }
}