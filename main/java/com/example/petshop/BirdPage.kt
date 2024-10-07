package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityBirdPageBinding
import com.example.petshop.databinding.ActivityDogPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BirdPage : AppCompatActivity() {

    private lateinit var binding: ActivityBirdPageBinding
    private lateinit var birdListAdapter: BirdAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirdPageBinding.inflate(layoutInflater)
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

        val nameList = arrayOf("Budgie","Menu", "Rio", "Peanuts")
        val priceList = arrayOf("30", "60", "100", "90")

        val imageList = intArrayOf(
            R.drawable.b1,
            R.drawable.b2,
            R.drawable.b3,
            R.drawable.b4
        )

        val descList = intArrayOf(
            R.string.bird1,
            R.string.bird2,
            R.string.bird3,
            R.string.bird4
        )

        for (i in imageList.indices){
            listData = ListData(nameList[i], priceList[i], descList[i], imageList[i])
            dataArrayList.add(listData)
        }

        birdListAdapter = BirdAdapter(this@BirdPage, dataArrayList)
        binding.birdListView.adapter = birdListAdapter
        binding.birdListView.isClickable = true

        binding.birdListView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this@BirdPage, BirdItemPage::class.java)
            intent.putExtra("name", nameList[i])
            intent.putExtra("price", priceList[i])
            intent.putExtra("desc", descList[i])
            intent.putExtra("image", imageList[i])
            startActivity(intent)
        }

    }
}