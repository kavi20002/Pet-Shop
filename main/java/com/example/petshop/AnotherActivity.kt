package com.example.petshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityAnotherBinding
import com.example.petshop.models.LoginFormData
import com.example.petshop.models.validations.ValidationResults
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class AnotherActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAnotherBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val isLoggedIn  = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        if (isLoggedIn){
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }

        binding.SignInBtn.setOnClickListener{
            validateAndLogin()
        }

        val textView = findViewById<TextView>(R.id.xml_text)
        val text =  "If You are not register User ?"
        val spanString = SpannableString(text)

        val registerText = object : ClickableSpan(){
            override fun onClick(widget : View) {
                val intent = Intent(this@AnotherActivity, SignUpPage::class.java)
                startActivity(intent)
            }
        }

        spanString.setSpan(registerText,  0 , text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spanString
        textView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

    }

    private fun validateAndLogin(){
        val enteredUserName = binding.username.text.toString().trim()
        val enteredPassword = binding.password.text.toString().trim()

        val loginForm = LoginFormData(enteredUserName, enteredPassword)

        val usernameValidation = loginForm.validateUserName()
        when(usernameValidation){
            is ValidationResults.Valid -> {}
            is ValidationResults.Invalid -> binding.username.error = usernameValidation.errorMessage
            is ValidationResults.Empty -> binding.username.error = usernameValidation.errorMessage
        }

        val passwordValidation = loginForm.validatePassword()
        when(passwordValidation){
            is ValidationResults.Valid -> {}
            is ValidationResults.Invalid -> binding.password.error = passwordValidation.errorMessage
            is ValidationResults.Empty -> binding.password.error = passwordValidation.errorMessage
        }

        if (usernameValidation is ValidationResults.Valid && passwordValidation is ValidationResults.Valid){
            login(enteredUserName, enteredPassword)
        }
    }

    private fun login(username: String, password: String){
        val savedPassword = sharedPreferences.getString("${username}_PASSWORD", null)
        val savedUsername = sharedPreferences.getString("${username}_USERNAME", null)

        val hashedPassword = hashPassword(password)

        if(savedUsername != null && savedPassword != null && hashedPassword == savedPassword){

            sharedPreferences.edit()
                .putBoolean("IS_LOGGED_IN", true)
                .putString("LOGGED_IN_USERNAME", username)
                .apply()

            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, "Login Failed. Please check your username and password.", Toast.LENGTH_LONG).show()
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash  = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return hash.joinToString(""){"%02x".format(it)}
    }


}