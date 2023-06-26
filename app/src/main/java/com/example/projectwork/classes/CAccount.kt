package com.example.projectwork.classes

data class CAccount(
    val accountID: Int,
    val email: String,
    val name : String,
    val surname : String,
    val username : String,
    val password : String,
    val profileImage : String,
    val steps : Int,
    val bio : String,
    val friends : MutableList<Int>?,
    val longitude : Double,
    val latitude : Double,
)
