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

class FavouritePAge : AppCompatActivity() {

    private lateinit var logOut : Button
    private  lateinit var  profileBtn : ImageView

    private lateinit var greetingTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_page)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navBar)

        bottomNavigationView.selectedItemId = R.id.bottom_fav

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    true
                }
                R.id.bottom_fav -> true
                R.id.bottom_cart -> {
                    startActivity(Intent(applicationContext,CartPage::class.java))
                    true
                }
                R.id.bottom_fav -> true
                R.id.bottom_search -> {
                    startActivity(Intent(applicationContext,BlogPage::class.java))
                    true
                }

                else -> false
            }
        }

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        greetingTextView = findViewById(R.id.greeting_text)

        val username = sharedPreferences.getString("LOGGED_IN_USERNAME", "User")
        greetingTextView.text = "Hi, $username "

        logOut = findViewById(R.id.logOut)
        profileBtn = findViewById(R.id.profile_image)

        logOut.setOnClickListener{
            logout()
        }

        profileBtn.setOnClickListener{
            val intent = Intent(this, MyProfilePage::class.java)
            startActivity(intent)
        }
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