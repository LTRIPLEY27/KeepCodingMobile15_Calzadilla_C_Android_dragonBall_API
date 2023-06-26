package com.example.dragonball_api_kc_android.model

data class Heroe (
    val name: String,
    val favorite : Boolean,
    val photo : String,
    val description : String
){
    var selected = false
    var maxLife : Int = 100
    var actualLife : Int = 100
}