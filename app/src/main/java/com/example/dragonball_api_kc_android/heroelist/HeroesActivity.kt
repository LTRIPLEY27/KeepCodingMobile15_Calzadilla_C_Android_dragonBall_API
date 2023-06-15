package com.example.dragonball_api_kc_android.heroelist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.dragonball_api_kc_android.databinding.ActivityHeroesBinding

class HeroesActivity : AppCompatActivity() {

    private val  heroesListViewModel : HeroesListViewModel by viewModels()
    private lateinit var heroeList : ActivityHeroesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heroeList = ActivityHeroesBinding.inflate(layoutInflater);
        setContentView(heroeList.root)
        Log.i("HEROE_LIST", "HAS LLEGADO HASTA ACÁ")
        insertFragment()
    }

    private fun insertFragment(){
        // LLAMADO DE LA TRANSACCIÓN PARA INSERTAR EL FRAGMENTLIST EN LA ACTIVITY PRINCIPAL MEDIANTE EL CONTAINERFRAGMEN
        // SE LE DEBE DE INDICAR EL ID DEL CONTAINERFRAGMENT Y LA ACTIVITY DEL FRACGMENT A INSERTAR
        supportFragmentManager.beginTransaction()
            .replace(heroeList.frContainer .id, FragmentList())
            .commit()

    }
}