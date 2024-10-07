package com.example.petshop.models

import com.example.petshop.models.validations.ValidationResults

class LoginFormData(
    private val userName: String,
    private val password: String
) {

    fun validateUserName() : ValidationResults{
        return when {
            userName.isEmpty() -> ValidationResults.Empty("Username is required")
            userName.length != 6 -> ValidationResults.Invalid("Username must be contain 6 characters long")
            else -> ValidationResults.Valid
        }
    }

    fun validatePassword(): ValidationResults{
        return  when{
            password.isEmpty() -> ValidationResults.Empty("Password is required")
            password.length != 6 -> ValidationResults.Invalid("Password must be contain 6 characters")
            else -> ValidationResults.Valid
        }
    }



}