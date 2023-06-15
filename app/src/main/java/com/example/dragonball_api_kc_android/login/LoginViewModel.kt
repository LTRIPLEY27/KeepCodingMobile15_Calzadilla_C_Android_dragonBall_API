package com.example.dragonball_api_kc_android.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonball_api_kc_android.persistence.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginViewModel : ViewModel() {

    // DECLARACIÓN DE LA UISTATE PARA ENLAZAR
    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState : StateFlow<LoginState> = _uiState

    // FUNCIÓN PARA CONECTAR A BASE DE DATOS EL LOGIN
    fun checkLogin(login: LoginDTO){
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}/api/auth/login"
            val loginKC = Credentials.basic(login.username, login.password)
            val body = FormBody.Builder()
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader(AUTHORIZATION, loginKC)
                .post(body)
                .build()
            val call = client.newCall(request)
            val response = call.execute()

            // INVOCACIÓN DE LOS OYENTES CONDICIONANDO A SI HA SIDO O NO SATISFACTORIA LA REUQEST
            _uiState.value = if (response.isSuccessful)
                response.body?.let {
                    LoginState.OnTokenReceived(it.string())
                } ?: LoginState.Error("Not Token Found")
            else
                LoginState.Error(response.message)

            Log.i("TOKEN", "TOKEN RECEIVED: " + _uiState.value)
        }
    }
/*
    // función para capturar todos los heroes
    fun loadHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "$URL/api/heros/all"
            val body = FormBody.Builder()
                .add("name", "")
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader(AUTHORIZATION, "Bearer $token")
                .post(body)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            println(response.body)

            response.body?.let { it ->
                val gson = Gson()
                try{
                    val heroeDTOArray = gson.fromJson(it.toString(), Array<HeroeDTO>::class.java)
                    _uiState.value = UiState.OnHeroListReceived(heroeDTOArray.toList().map { Heroe(it.name, it.favorite, it.photo) })
                }
                catch (ex: Exception) {
                    _uiState.value = UiState.Error("Something went wrong in the request")
                }
            } ?: run { _uiState.value = UiState.Error("Something went wrong in the request") }

        }
    }
*/
    /**
     *  DECLARACIÓN DE LAS DIFERENTES STATES DE LOGINSTATE PARA LA REACTIVIDAD
     */
    sealed class LoginState {
        object Idle : LoginState()
        data class Error(val error : String) : LoginState()
        data class OnTokenReceived(val token : String) : LoginState()

        //data class OnHeroListReceived(val heroes : List<Heroe>) : LoginState()
    }

    companion object {
        const val BASE_URL = "https://dragonball.keepcoding.education"
        const val AUTHORIZATION = "Authorization"
    }
}