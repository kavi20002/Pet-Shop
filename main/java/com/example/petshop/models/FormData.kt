package com.example.petshop.models

import android.util.Patterns
import com.example.petshop.models.validations.ValidationResults

class FormData(
    private var userName: String,
    private var password: String,
    private var email: String,
    private var address: String
) {
    fun validateUserName(): ValidationResults{
        return  if (userName.isEmpty()){
            ValidationResults.Empty("Enter the User Name")
        }else if (userName.length!= 6){
            ValidationResults.Invalid("user name must contain 6 characters")
        }else{
            ValidationResults.Valid
        }

    }

    fun validatePassword(): ValidationResults{
        return if (password.isEmpty()){
            ValidationResults.Empty("Enter the password")
        }else  if (password.length != 6){
            ValidationResults.Invalid("password must contain 6 characters")
        }else{
            ValidationResults.Valid
        }
    }

    fun validateEmail(): ValidationResults{
        return if(email.isEmpty()){
            ValidationResults.Empty("Enter the Email")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ValidationResults.Invalid("Invalid Email format")
        }else{
            ValidationResults.Valid
        }
    }

    fun validateAddress(): ValidationResults{
        return if (address.isEmpty()){
            ValidationResults.Empty("Enter the Address")
        }else{
            ValidationResults.Valid
        }
    }



}