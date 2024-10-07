package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog


class CartPage : AppCompatActivity(), AdapterClass.OnQuantityChangeListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<ItemData>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>

    private val deliveryFee: Double = 350.00
    private lateinit var subTotalView: TextView
    private lateinit var totalFeeView: TextView
    private lateinit var deliveryFeeView: TextView
    private lateinit var addressBtn: ImageView
    private lateinit var paySelectBtn: ImageView
    private lateinit var orderNowBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_page)

        bottomNavigationView = findViewById(R.id.navBar)
        subTotalView = findViewById(R.id.subtotalFee)
        totalFeeView = findViewById(R.id.totalFee)
        deliveryFeeView = findViewById(R.id.deliveryFee)
        addressBtn = findViewById(R.id.addressBtn)
        paySelectBtn = findViewById(R.id.paySelectBtn)
        orderNowBtn = findViewById(R.id.payBtn)

        bottomNavigationView.selectedItemId = R.id.bottom_cart

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_fav -> {
                    startActivity(Intent(applicationContext, FavouritePAge::class.java))
                    true
                }
                R.id.bottom_home -> true
                R.id.bottom_home ->{
                    startActivity(Intent(applicationContext,HomePage::class.java))
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

        imageList = arrayOf(
            R.drawable.pedigree,
            R.drawable.meowmix,
            R.drawable.food1
        )

        titleList = arrayOf(
            "Pedigree",
            "MeowMix",
            "Nuts"
        )

        recyclerView = findViewById(R.id.cartView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        itemList = arrayListOf<ItemData>()
        getData()

        calculateFees()

        recyclerView.adapter = AdapterClass(itemList, this)

        addressBtn.setOnClickListener{
            showAddressDialog()
        }

        paySelectBtn.setOnClickListener{
            showPaymentDialog()
        }

        loadSavedData()

        orderNowBtn.setOnClickListener{
            Toast.makeText(this, "Order Successfully", Toast.LENGTH_LONG).show()
        }






    }

    private fun getData(){
        for(i in imageList.indices){
            val itemClass = ItemData(
                Image = imageList[i],
                title = titleList[i],
                feeEachItem = 10.0,
                totalEachItem =  10.0,
                quantity = 1
            )
            itemList.add(itemClass)
        }

    }

    private fun calculateFees(){

        var subTotal = 0.0
        for (item in itemList){
            subTotal += item.totalEachItem
        }

        val totalFee = subTotal + deliveryFee

        val locale = Locale.US
        subTotalView.text = String.format(locale,"%.2f", subTotal)
        deliveryFeeView.text = String.format(locale,"%.2f", deliveryFee)
        totalFeeView.text = String.format(locale, "%.2f", totalFee)
    }

    override fun onQuantityChanged() {
        calculateFees()
    }

    private fun showAddressDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Delivery Address")

        val input = EditText(this)
        input.hint = "Your Address"
        builder.setView(input)

        builder.setPositiveButton("Save"){ dialog, _ ->

            val address = input.text.toString()

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user_address", address)
            editor.apply()

            findViewById<TextView>(R.id.deliveryAddress).text = address

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel"){ dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showPaymentDialog(){
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_payment, null)

        val cashOption = view.findViewById<RadioButton>(R.id.cash)
        val cardOption = view.findViewById<RadioButton>(R.id.Card)

        bottomSheetDialog.setContentView(view)

        view.findViewById<Button>(R.id.btnSelectPayment).setOnClickListener{
            val selectedPayment = when {
                cashOption.isChecked -> "Cash"
                cardOption.isChecked -> "Credit Card"
                else -> "Cash"
            }

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("payment_method", selectedPayment)
            editor.apply()

            findViewById<TextView>(R.id.paymentMethod).text = selectedPayment

            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun loadSavedData(){
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val savedAddress = sharedPreferences.getString("user_address", "Address")
        val savedPaymentMethod = sharedPreferences.getString("payment_method", "Cash")

        findViewById<TextView>(R.id.deliveryAddress).text = savedAddress
        findViewById<TextView>(R.id.paymentMethod).text = savedPaymentMethod
    }


}

