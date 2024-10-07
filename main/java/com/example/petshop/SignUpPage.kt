package com.example.petshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.models.FormData
import com.example.petshop.models.validations.ValidationResults
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class SignUpPage : AppCompatActivity() {

    private lateinit var edtUserName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtAddress: EditText

    private lateinit var btnSignUp : Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        edtUserName = findViewById(R.id.edtUserName)
        edtPassword = findViewById(R.id.edtPassword)
        edtEmail = findViewById(R.id.edtEmail)
        edtAddress = findViewById(R.id.edtAddress)
        btnSignUp = findViewById(R.id.btnSignUp)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        btnSignUp.setOnClickListener{
            submit()
        }
    }

    private fun displayAlert(title: String, message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") {dialog, which ->
            dialog.dismiss()
        }

        builder.create().show()

    }

    private fun submit() {
        var count = 0

        val myForm = FormData(
            edtUserName.text.toString().trim(),
            edtPassword.text.toString().trim(),
            edtEmail.text.toString().trim(),
            edtAddress.text.toString().trim()
        )

        val userNameValidation = myForm.validateUserName()
        val passwordValidation = myForm.validatePassword()
        val emailValidation = myForm.validateEmail()
        val addressValidation = myForm.validateAddress()


        when (userNameValidation) {
            is ValidationResults.Valid -> count++
            is ValidationResults.Invalid -> edtUserName.error = userNameValidation.errorMessage
            is ValidationResults.Empty -> edtUserName.error = userNameValidation.errorMessage

        }

        when (passwordValidation) {
            is ValidationResults.Valid -> count++
            is ValidationResults.Invalid -> edtPassword.error = passwordValidation.errorMessage
            is ValidationResults.Empty -> edtPassword.error = passwordValidation.errorMessage
        }

        when (emailValidation) {
            is ValidationResults.Valid -> count++
            is ValidationResults.Invalid -> edtEmail.error = emailValidation.errorMessage
            is ValidationResults.Empty -> edtEmail.error = emailValidation.errorMessage
        }

        when (addressValidation) {
            is ValidationResults.Valid -> count++
            is ValidationResults.Invalid -> edtAddress.error = addressValidation.errorMessage
            is ValidationResults.Empty -> edtAddress.error = addressValidation.errorMessage
        }

        if (count == 4) {

            val username = edtUserName.text.toString()

            val existingUser = sharedPreferences.getString("${username}_USERNAME", null)

            if (existingUser != null){
                displayAlert("Error", "Username already exists. Please choose a different one.")
            }else{
                val editor = sharedPreferences.edit()

                editor.putString("${username}_USERNAME", username)
                editor.putString("${username}_PASSWORD", hashPassword(edtPassword.text.toString()))
                editor.putString("${username}_EMAIL", edtEmail.text.toString())
                editor.putString("${username}_ADDRESS", edtAddress.text.toString())
                editor.apply()

                displayAlert("Success", "You have successfully registered!")
                val intent = Intent(this, AnotherActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun hashPassword(password: String) : String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return hash.joinToString(""){"%02x".format(it)}
    }


}