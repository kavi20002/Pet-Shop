package com.example.petshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity



class Second_Page : AppCompatActivity() {

    private lateinit var myAdapter: MyAdapter
    private lateinit var dotsTv: Array<TextView?>
    private lateinit var layouts:IntArray
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout:LinearLayout
    private lateinit var btnNext:Button
    private lateinit var btnSkip:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (!isFirstTimeAppStart()){
//            setAppStartStatus(false)
//            startActivity(Intent(this, AnotherActivity::class.java))
//            finish()
//        }


        setContentView(R.layout.activity_second_page)

        viewPager = findViewById(R.id.viewPager)
        dotsLayout = findViewById(R.id.dotsLayout)
        btnNext = findViewById(R.id.btn_next)
        btnSkip = findViewById(R.id.btn_skip)

        supportActionBar?.hide()



        statusBarTransparent()

        btnNext.setOnClickListener{
            val currentPage : Int = viewPager.currentItem + 1

            if(currentPage < layouts.size){
                viewPager.currentItem = currentPage
            }
            else{
                setAppStartStatus(false)
                startActivity(Intent(this, AnotherActivity::class.java))
                finish()
            }

        }

        btnSkip.setOnClickListener{
            setAppStartStatus(false)
            startActivity(Intent(this, AnotherActivity::class.java))
            finish()
        }

        layouts = intArrayOf(R.layout.slide_4, R.layout.slide_2, R.layout.slide_3)
        myAdapter = MyAdapter(layouts, applicationContext)
        viewPager.adapter = myAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == layouts.size -1){
                    btnNext.text = "START"
                    btnSkip.visibility = View.GONE
                }else{
                    btnNext.text = "NEXT"
                    btnSkip.visibility = View.VISIBLE
                }
                setDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        setDots(0)

    }

//    private fun isFirstTimeAppStart() : Boolean {
//        val pref = applicationContext.getSharedPreferences( "Pet Shop", Context.MODE_PRIVATE)
//        return  pref.getBoolean("App_START",true)
//    }

    private fun setAppStartStatus(status: Boolean){
        val pref = applicationContext.getSharedPreferences( "Pet Shop", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.putBoolean("App_START", status)
        editor.apply()
    }

    private fun statusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setDots(page: Int){
        dotsLayout.removeAllViews()
        dotsTv = arrayOfNulls(layouts.size)

        for (i in dotsTv.indices){
            dotsTv[i] = TextView(this)
            dotsTv[i]!!.text = Html.fromHtml("&#8226")
            dotsTv[i]!!.textSize = 30f
            dotsTv[i]!!.setTextColor(Color.parseColor("#3E6196"))
            dotsLayout.addView(dotsTv[i])
        }

        if (dotsTv.isNotEmpty()){
            dotsTv[page]!!.setTextColor(Color.parseColor("#ffffff"))
        }
    }
}