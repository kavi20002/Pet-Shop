package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.petshop.databinding.ActivityAccessoryItemPageBinding
import com.example.petshop.databinding.ActivityItemPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccessoryItemPage : AppCompatActivity() {

    private lateinit var backBtn : ImageView
    private lateinit var cartBtn : Button
    private lateinit var favouriteBtn : ImageView
    private lateinit var binding: ActivityAccessoryItemPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessoryItemPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backBtn = findViewById(R.id.arrow)
        cartBtn = findViewById(R.id.cartBtn)
        favouriteBtn = findViewById(R.id.favourite)

        backBtn.setOnClickListener(){
            val intent = Intent(this, AccessoriesPage::class.java)
            startActivity(intent)
        }

        cartBtn.setOnClickListener(){
            addToCart("Pedigree", R.drawable.pedigree, 22.0)
        }

        favouriteBtn.setOnClickListener(){
            Toast.makeText(this,"Add Item to favorite", Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navBar)

        bottomNavigationView.selectedItemId = R.id.bottom_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    true
                }
                R.id.bottom_cart -> true
                R.id.bottom_fav -> {
                    startActivity(Intent(applicationContext, FavouritePAge::class.java))
                    true
                }
                R.id.bottom_cart -> true
                R.id.bottom_search -> {
                    startActivity(Intent(applicationContext,BlogPage::class.java))
                    true
                }
                R.id.bottom_cart -> {
                    startActivity(Intent(applicationContext, CartPage::class.java))
                    true
                }
                else -> false
            }
        }

        val intent  = this.intent
        if (intent != null){
            val name = intent.getStringExtra("name")
            val price = intent.getStringExtra("price")
            val desc = intent.getIntExtra("desc", R.string.ac1)
            val image = intent.getIntExtra("image", R.drawable.a1)

            binding.accessoryitemTitle.text = name
            binding.accessoryItemsPrice.text = price
            binding.accessoryDesc.setText(desc)
            binding.accessoryitemImage.setImageResource(image)
        }

    }

    private fun addToCart(itemTitle: String, itemImage: Int, itemPrice: Double){
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val currentCart = sharedPreferences.getStringSet("cartItems", mutableSetOf()) ?: mutableSetOf()

        val cartItem = "$itemTitle, $itemImage, $itemPrice"

        currentCart.add(cartItem)

        editor.putStringSet("cartItems", currentCart)
        editor.apply()

        Toast.makeText(this,"Add Item to Cart", Toast.LENGTH_LONG).show()

    }
}