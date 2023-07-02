package com.example.dragonball_api_kc_android.model

import kotlin.random.Random

data class Heroe (
    val name: String,
    val favorite : Boolean,
    val photo : String,
    val description : String,
    var maxLife : Int = 100,
    var actualLife : Int = 100
){
    var selected = false

}