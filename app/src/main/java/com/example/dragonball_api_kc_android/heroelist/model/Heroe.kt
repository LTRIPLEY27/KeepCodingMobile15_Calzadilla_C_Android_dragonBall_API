package com.example.dragonball_api_kc_android.heroelist.model

data class Heroe (
    val name: String,
    val favorite : Boolean,
    val photo : String
){
    var selected = false
    var maxLife : Int = 100
    var actualLife : Int = 100
}