package com.example.petshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class HomePage : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var dogBtn: ImageView
    private lateinit var catBtn: ImageView
    private lateinit var birdBtn: ImageView
    private lateinit var accessoryBtn: ImageView
    private  lateinit var  profileBtn : ImageView
    private lateinit var pedigreeBtn : ImageView
    private lateinit var logOut : Button
    private lateinit var contactBtn : Button
    private lateinit var taskBtn: Button

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var greetingTextView: TextView
    private lateinit var greetingTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        bottomNavigationView = findViewById(R.id.navBar)

        bottomNavigationView.selectedItemId = R.id.bottom_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_fav -> {
                    startActivity(Intent(applicationContext, FavouritePAge::class.java))
                    true
                }
                R.id.bottom_home -> true
                R.id.bottom_cart ->{
                    startActivity(Intent(applicationContext,CartPage::class.java))
                    true
                }
                R.id.bottom_home -> true
                R.id.bottom_search ->{
                    startActivity(Intent(applicationContext,BlogPage::class.java))
                    true
                }
                else -> false
            }
        }

        dogBtn = findViewById(R.id.dogBtn)
        catBtn = findViewById(R.id.catBtn)
        birdBtn = findViewById(R.id.birdBtn)
        accessoryBtn = findViewById(R.id.accessoryBtn)
        profileBtn = findViewById(R.id.profile_image)
        pedigreeBtn = findViewById(R.id.pedigree)
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        greetingTextView = findViewById(R.id.greeting_text)
        greetingTimeTextView = findViewById(R.id.greeting)
        taskBtn  = findViewById(R.id.taskBtn)

        val username = sharedPreferences.getString("LOGGED_IN_USERNAME", "User")
        greetingTextView.text = "Hi, $username "

        setGreetingTimeMessage()

        dogBtn.setOnClickListener{
            val intent = Intent(this, DogPage::class.java)
            startActivity(intent)
        }

        catBtn.setOnClickListener{
            val intent = Intent(this, CatPage::class.java)
            startActivity(intent)
        }

        birdBtn.setOnClickListener{
            val intent = Intent(this, BirdPage::class.java)
            startActivity(intent)
        }

        accessoryBtn.setOnClickListener{
            val intent = Intent(this, AccessoriesPage::class.java)
            startActivity(intent)
        }

        profileBtn.setOnClickListener{
            val intent = Intent(this, MyProfilePage::class.java)
            startActivity(intent)
        }

        pedigreeBtn.setOnClickListener{
            val intent = Intent(this, ItemPage::class.java)
            startActivity(intent)
        }

        logOut = findViewById(R.id.logOut)
        contactBtn = findViewById(R.id.contact)

        logOut.setOnClickListener{
            logout()
        }

        contactBtn.setOnClickListener{
            val intent = Intent(this,ContactUsPage::class.java)
            startActivity(intent)
        }

        taskBtn.setOnClickListener{
            val intent = Intent(this, TaskPage::class.java)
            startActivity(intent)
        }



    }

    private fun setGreetingTimeMessage(){
        val calendar = Calendar.getInstance()
        val hourofDay = calendar.get(Calendar.HOUR_OF_DAY)

        val greetingMessage = when {
            hourofDay in 5..11 -> "Good Morning"
            hourofDay in 12 .. 16 -> "Good Afternoon"
            hourofDay in 17 .. 20 -> "Good Evening"
            else -> "Good Night"
        }

        greetingTimeTextView.text = greetingMessage
    }

    private fun logout(){
        val editor = sharedPreferences.edit()
        editor.remove("LOGGED_IN_USERNAME")
        editor.putBoolean("IS_LOGGED_IN", false)
        editor.apply()

        val intent = Intent(this, AnotherActivity::class.java)
        startActivity(intent)

        Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show()

        finish()
    }


}