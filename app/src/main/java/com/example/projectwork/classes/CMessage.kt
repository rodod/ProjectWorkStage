package com.example.projectwork.classes

import java.util.Date

data class CMessage(
    val messageID : Int,
    val receiverID : Int,
    val senderUsername : String,
    val senderID : Int,
    val message : String,
    val steps : Int,
    val date : Date,
    val content : String,
)