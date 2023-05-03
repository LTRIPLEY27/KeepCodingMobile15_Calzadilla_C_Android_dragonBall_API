package com.example.dragonball_api_kc_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dragonball_api_kc_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     *  PROPIEDAD 'BINDING' PARA ENLAZAR AL LAYOUT
     */
    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEFINICIÓN DE LAS PROPIEDADES DEL LAYOUT AL BINDING
        binding = ActivityMainBinding.inflate(layoutInflater);
        // INDICACIÓN DEL CONTENTVIEW CON EL VALOR DEL BINDING
        setContentView(binding.root)
    }
}