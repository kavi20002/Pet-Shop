package com.example.petshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView



class MyProfilePage : AppCompatActivity() {

    private lateinit var textViewUsername : TextView
    private lateinit var textViewEmail : TextView
    private lateinit var textViewAddress : TextView
    private lateinit var textViewMobileNumber: TextView
    private lateinit var textViewPostalCode: TextView
    private lateinit var editUsername  :EditText
    private lateinit var editEmail: EditText
    private lateinit var editAddress: EditText
    private lateinit var editMobileNumber: EditText
    private lateinit var editPostalCode:EditText
    private lateinit var changeUserName: TextView
    private lateinit var changeEmail : TextView
    private lateinit var changeAddress: TextView
    private lateinit var changeMobileNumber: TextView
    private lateinit var changePostalCode: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var saveBtn : Button
    private lateinit var  profileImageView: ImageView
    private  val REQUEST_CODE_STORAGE_PERMISSION  = 100
    private val REQUEST_CODE_PICK_IMAGE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_page)

        textViewUsername = findViewById(R.id.textView6)
        textViewEmail = findViewById(R.id.textView7)
        textViewAddress = findViewById(R.id.textView20)
        textViewMobileNumber = findViewById(R.id.mobileNumber)
        textViewPostalCode = findViewById(R.id.postalCode)
        editUsername = findViewById(R.id.editUSerName)
        editEmail = findViewById(R.id.editEmail)
        editAddress = findViewById(R.id.editAddress)
        editMobileNumber = findViewById(R.id.editMobile)
        editPostalCode = findViewById(R.id.editPostalCode)
        changeUserName = findViewById(R.id.textView24)
        changeEmail = findViewById(R.id.textView27)
        changeAddress = findViewById(R.id.textView28)
        changeMobileNumber = findViewById(R.id.mobileNumber)
        changePostalCode = findViewById(R.id.postalCode)
        saveBtn = findViewById(R.id.saveBtn)
        profileImageView = findViewById(R.id.profileImage)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val savedImageUri = sharedPreferences.getString("profile_image_uri", null)
        if (savedImageUri != null) {
            profileImageView.setImageURI(Uri.parse(savedImageUri))
        }

        profileImageView.setOnClickListener {
            requestStoragePermission()
        }

        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        if (!isLoggedIn){
            val intent = Intent(this, AnotherActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val loggedInUsername = sharedPreferences.getString("LOGGED_IN_USERNAME",  "User") ?: "User"

        val username = sharedPreferences.getString("${loggedInUsername}_USERNAME", "Not Available")
        val email = sharedPreferences.getString("${loggedInUsername}_EMAIL", "Not Available")
        val address = sharedPreferences.getString("${loggedInUsername}_ADDRESS", "Not Available")
        val mobileNumber = sharedPreferences.getString("${loggedInUsername}_MOBILE_NUMBER", "Not Available")
        val postalCode = sharedPreferences.getString("${loggedInUsername}_POSTAL_CODE", "Not Available")

        textViewUsername.text = username
        textViewEmail.text = email
        textViewAddress.text = address
        textViewMobileNumber.text = mobileNumber
        textViewPostalCode.text = postalCode

        editUsername.visibility = EditText.GONE
        editEmail.visibility = EditText.GONE
        editAddress.visibility = EditText.GONE
        editMobileNumber.visibility = EditText.GONE
        editPostalCode.visibility = EditText.GONE

        changeUserName.setOnClickListener{
            toggleEditVisibility(editUsername, textViewUsername)
        }

        changeEmail.setOnClickListener{
            toggleEditVisibility(editEmail, textViewEmail)
        }

        changeAddress.setOnClickListener{
            toggleEditVisibility(editAddress, textViewAddress)
        }

        changeMobileNumber.setOnClickListener{
            toggleEditVisibility(editMobileNumber, textViewMobileNumber)
        }

        changePostalCode.setOnClickListener{
            toggleEditVisibility(editPostalCode, textViewPostalCode)
        }


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



        saveBtn.setOnClickListener{
            saveChanges(loggedInUsername)
        }

    }

    private fun toggleEditVisibility(editText: EditText, textView: TextView){
        if (editText.visibility == EditText.GONE){
            editText.visibility = EditText.VISIBLE
            textView.visibility = TextView.GONE
            editText.setText(textView.text)
        }else {
            editText.visibility = EditText.GONE
            textView.visibility = TextView.VISIBLE
        }
    }

    private fun saveChanges(loggedInUsername: String){
        val currentUserName = sharedPreferences.getString("${loggedInUsername}_USERNAME", "")
        val currentEmail = sharedPreferences.getString("${loggedInUsername}_EMAIL", "")
        val currentAddress = sharedPreferences.getString("${loggedInUsername}_ADDRESS", "")
        val currentMobileNumber = sharedPreferences.getString("${loggedInUsername}_MOBILE_NUMBER", "")
        val currentPostalCode = sharedPreferences.getString("${loggedInUsername}_POSTAL_CODE", "")

        val newUsername = editUsername.text.toString()
        val newEmail  = editEmail.text.toString()
        val newAddress = editAddress.text.toString()
        val newMobileNumber = editMobileNumber.text.toString()
        val newPostalCode = editPostalCode.text.toString()

        val editor = sharedPreferences.edit()

        if (newUsername != currentUserName && newUsername.isNotEmpty()){
            editor.putString("${loggedInUsername}_USERNAME", newUsername)
            textViewUsername.text = newUsername
        }

        if (newEmail != currentEmail && newEmail.isNotEmpty()){
            editor.putString("${loggedInUsername}_EMAIL", newEmail)
            textViewEmail.text = newEmail
        }

        if (newAddress != currentAddress && newAddress.isNotEmpty()){
            editor.putString("${loggedInUsername}_ADDRESS", newAddress)
            textViewAddress.text = newAddress
        }

        if (newMobileNumber != currentMobileNumber && newMobileNumber.isNotEmpty()){
            editor.putString("${loggedInUsername}_MOBILE_NUMBER", newMobileNumber)
            textViewMobileNumber.text = newMobileNumber
        }

        if (newPostalCode != currentPostalCode && newPostalCode.isNotEmpty()){
            editor.putString("${loggedInUsername}_POSTAL_CODE", newPostalCode)
            textViewPostalCode.text = newPostalCode
        }

        editor.apply()

        editUsername.visibility = EditText.GONE
        editEmail.visibility = EditText.GONE
        editAddress.visibility = EditText.GONE
        editMobileNumber.visibility = EditText.GONE
        editPostalCode.visibility = EditText.GONE

        textViewUsername.visibility = TextView.VISIBLE
        textViewEmail.visibility = TextView.VISIBLE
        textViewAddress.visibility = TextView.VISIBLE
        textViewMobileNumber.visibility = TextView.VISIBLE
        textViewPostalCode.visibility = TextView.VISIBLE



        Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_LONG).show()


    }

    private fun requestStoragePermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
        } else {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Call the super method

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGalleryForImage()
        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                profileImageView.setImageURI(imageUri)
                sharedPreferences.edit().putString("profile_image_uri", imageUri.toString()).apply()
            }
        }
    }






}