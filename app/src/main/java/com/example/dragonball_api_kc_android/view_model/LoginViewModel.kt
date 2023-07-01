package com.example.dragonball_api_kc_android.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonball_api_kc_android.model.LoginDTO
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
            val url = "$BASE_URL/api/auth/login"
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

    /**
     *  DECLARACIÓN DE LAS DIFERENTES STATES DE LOGINSTATE PARA LA REACTIVIDAD
     */
    sealed class LoginState {
        object Idle : LoginState()
        data class Error(val error : String) : LoginState()
        data class OnTokenReceived(val token : String) : LoginState()

    }

    companion object {
        const val BASE_URL = "https://dragonball.keepcoding.education"
        const val AUTHORIZATION = "Authorization"
    }
}