package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class ContactUsPage : AppCompatActivity() {

    private  lateinit var submitBtn : Button
    private  lateinit var  profileBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us_page)

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

        submitBtn = findViewById(R.id.submit)
        profileBtn = findViewById(R.id.profile_image)

        submitBtn.setOnClickListener(){
            Toast.makeText(this,"Submit Successfully", Toast.LENGTH_LONG).show()
        }

        profileBtn.setOnClickListener(){
            val intent = Intent(this, MyProfilePage::class.java)
            startActivity(intent)
        }

    }
}