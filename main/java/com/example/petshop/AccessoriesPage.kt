package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityAccessoriesPageBinding
import com.example.petshop.databinding.ActivityDogPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AccessoriesPage : AppCompatActivity() {
    private lateinit var binding: ActivityAccessoriesPageBinding
    private lateinit var listAdapter: AccessoryAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessoriesPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navBar)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_home ->{
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    true
                }
                R.id.bottom_fav -> {
                    startActivity(Intent(applicationContext, FavouritePAge::class.java))
                    true
                }
                R.id.bottom_cart -> {
                    startActivity(Intent(applicationContext,CartPage::class.java))
                    true
                }
                R.id.bottom_search -> {
                    startActivity(Intent(applicationContext,BlogPage::class.java))
                    true
                }
                else -> false
            }
        }

        val nameList = arrayOf("Neck Collar","Harnes", "Cat Collar", "Travel Bag")
        val priceList = arrayOf("30", "60", "100", "90")

        val imageList = intArrayOf(
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4
        )

        val descList = intArrayOf(
            R.string.ac1,
            R.string.ac2,
            R.string.ac3,
            R.string.ac4
        )

        for (i in imageList.indices){
            listData = ListData(nameList[i], priceList[i], descList[i], imageList[i])
            dataArrayList.add(listData)
        }

        listAdapter = AccessoryAdapter(this@AccessoriesPage, dataArrayList)
        binding.accessoryListView.adapter = listAdapter
        binding.accessoryListView.isClickable = true

        binding.accessoryListView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this@AccessoriesPage, AccessoryItemPage::class.java)
            intent.putExtra("name", nameList[i])
            intent.putExtra("price", priceList[i])
            intent.putExtra("desc", descList[i])
            intent.putExtra("image", imageList[i])
            startActivity(intent)
        }

    }
}