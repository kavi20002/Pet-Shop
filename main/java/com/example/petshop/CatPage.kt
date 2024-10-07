package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityCatPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class CatPage : AppCompatActivity() {

    private lateinit var binding: ActivityCatPageBinding
    private lateinit var listAdapter: CatAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navBar)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    true
                }

                R.id.bottom_fav -> {
                    startActivity(Intent(applicationContext, FavouritePAge::class.java))
                    true
                }

                R.id.bottom_cart -> {
                    startActivity(Intent(applicationContext, CartPage::class.java))
                    true
                }

                R.id.bottom_search -> {
                    startActivity(Intent(applicationContext,BlogPage::class.java))
                    true
                }

                else -> false
            }
        }

        val nameList = arrayOf("MeowMix","Friskies", "9Lives", "One")
        val priceList = arrayOf("30", "60", "100", "90")

        val imageList = intArrayOf(
            R.drawable.meowmix,
            R.drawable.cf1,
            R.drawable.cf2,
            R.drawable.cf3
        )

        val descList = intArrayOf(
            R.string.cat1,
            R.string.cat2,
            R.string.cat3,
            R.string.cat4
        )

        for (i in imageList.indices){
            listData = ListData(nameList[i], priceList[i], descList[i], imageList[i])
            dataArrayList.add(listData)
        }

        listAdapter = CatAdapter(this@CatPage, dataArrayList)
        binding.catListView.adapter = listAdapter
        binding.catListView.isClickable = true

        binding.catListView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this@CatPage, CatItemPage::class.java)
            intent.putExtra("name", nameList[i])
            intent.putExtra("price", priceList[i])
            intent.putExtra("desc", descList[i])
            intent.putExtra("image", imageList[i])
            startActivity(intent)
        }


    }


}