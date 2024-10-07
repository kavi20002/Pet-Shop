package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityDogPageBinding
import com.example.petshop.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DogPage : AppCompatActivity() {

    private lateinit var binding: ActivityDogPageBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogPageBinding.inflate(layoutInflater)
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

        val nameList = arrayOf("Pedigree","Kirkland", "Bakers", "Kibbles")
        val priceList = arrayOf("30", "60", "100", "90")

        val imageList = intArrayOf(
            R.drawable.pedigree,
            R.drawable.food2,
            R.drawable.food1,
            R.drawable.food3
        )

        val descList = intArrayOf(
            R.string.des1,
            R.string.des2,
            R.string.des3,
            R.string.des4
        )

        for (i in imageList.indices){
            listData = ListData(nameList[i], priceList[i], descList[i], imageList[i])
            dataArrayList.add(listData)
        }

        listAdapter = ListAdapter(this@DogPage, dataArrayList)
        binding.dogListView.adapter = listAdapter
        binding.dogListView.isClickable = true

        binding.dogListView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this@DogPage, ItemPage::class.java)
            intent.putExtra("name", nameList[i])
            intent.putExtra("price", priceList[i])
            intent.putExtra("desc", descList[i])
            intent.putExtra("image", imageList[i])
            startActivity(intent)
        }






    }
}