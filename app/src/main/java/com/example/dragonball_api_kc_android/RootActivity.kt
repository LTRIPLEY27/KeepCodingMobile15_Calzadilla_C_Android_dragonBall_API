package com.example.dragonball_api_kc_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dragonball_api_kc_android.databinding.ActivityLoginBinding
import com.example.dragonball_api_kc_android.heroelist.HeroesActivity
import com.example.dragonball_api_kc_android.heroelist.HeroesListViewModel
import com.example.dragonball_api_kc_android.login.LoginDTO
import com.example.dragonball_api_kc_android.login.LoginViewModel
import com.example.dragonball_api_kc_android.persistence.UserDetails
import kotlinx.coroutines.launch

class RootActivity : AppCompatActivity() {

    /**
     *  PROPIEDAD 'BINDING' PARA ENLAZAR AL LAYOUT
     */
    private lateinit var  binding : ActivityLoginBinding
    private lateinit var login : LoginDTO

    /**
     *  PROPIEDAD 'viewModelLogin' Obtiene el ViewModel reciclado y gestionado por GOOGLE, recuperando su estado y valor
     *  se ha de definir el 'by viewModels()' para aplicarlo.
     */
    private val  viewModelLogin : LoginViewModel by viewModels()
    private val  heroesViewModel : HeroesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEFINICIÓN DE LAS PROPIEDADES DEL LAYOUT AL BINDING
        binding = ActivityLoginBinding.inflate(layoutInflater);

        // INDICACIÓN DEL CONTENTVIEW CON EL VALOR DEL BINDING
        setContentView(binding.root)

        Toast.makeText(this, "Prueba", LENGTH_LONG).show();
        // LLAMADO A LA VERIFICACIÓN DEL LOGIN
        checkTheLog();
    }

    private fun checkTheLog(){
        binding.btLogin.setOnClickListener {
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){
                login = LoginDTO(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                Toast.makeText(this, binding.etPassword.text, LENGTH_LONG).show();
                viewModelLogin.checkLogin(login)
                callToValue()
            }
            else {
                Toast.makeText(this, "Ambos campos deben de contener valores", LENGTH_LONG).show();
            }
        }
    }

    private fun callToValue(){
        lifecycleScope.launch {
            viewModelLogin.uiState.collect{
                when(it) {
                    is LoginViewModel.LoginState.OnTokenReceived -> {
                        UserDetails.checkToken(it.token, this@RootActivity)
                        Log.i("DETAILS", "SUCESS" + it.token)
                        callHeroee()
                    }
                    is LoginViewModel.LoginState.Error -> {
                        Log.i("ERROR_LOGIN", "Hay un error de autenticación")
                    }
                    else -> {}
                }

            }
        }

    }

    fun callHeroee() {
        val intent = Intent(this, HeroesActivity::class.java)
        startActivity(intent)
    }
}