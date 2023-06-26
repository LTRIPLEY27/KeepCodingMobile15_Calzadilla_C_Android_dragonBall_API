package com.example.dragonball_api_kc_android.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonball_api_kc_android.model.Heroe
import com.example.dragonball_api_kc_android.model.HeroeDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class HeroesListViewModel : ViewModel() {

    //private lateinit var viewModelLogin : LoginViewModel

    private val _uiState = MutableStateFlow<HeroesState>(HeroesState.Idle)
    val uiState : StateFlow<HeroesState> = _uiState

    private val _uiStateDetail = MutableStateFlow<HeroeDetailState>(HeroeDetailState.Idle)
    val detailState : StateFlow<HeroeDetailState> = _uiStateDetail

    // lista MUTABLE DE HEROES
    private var heroes = mutableListOf<Heroe>()

    fun downloadHeroes(token : String) {
        Log.i("TOKEN_RECEIVED", "EL TOKEN ACÁ ES  $token")

        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "$BASE_URL/api/heros/all"
            val formBody = FormBody.Builder()
                .add("name", "")
                .build()
            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader(AUTHORIZATION, "Bearer $token")
                .build()
            val call = client.newCall(request)

            val response = call.execute()

            if(response.isSuccessful) {
                // si la respuesta ocurre, Deseriarizamos el objeto
                val heroesDTO : Array<HeroeDTO> = Gson()
                    .fromJson(response.body?.string(), Array<HeroeDTO>::class.java)

                //SETTEAMOS LOS VALORES DEL MODEL QUE USAREMOS CON LOS OBTENIDOS DESDE LA API, MAPPEANDO LOS MISMOS
                heroes.addAll(heroesDTO.map { Heroe(it.name, it.favorite, it.photo, it.description) })

                // INVOCAMOS AL OYENTE PARA QUE LLAME AL EVENTO
                _uiState.value = HeroesState.HeroesList(heroes)
                Log.i("HEROES", "Listado de heroes " + _uiState.value)
            } else {
                _uiState.value = HeroesState.Error(response.message)
                Log.i("HEROES_EMPTY", "Listado de heroes  VAAVIO" + _uiState.value)

            }
        }
    }

    /**
     *  DETALLE DEL HÉROE SELECCIONAD0
     * */
   fun getHero(heroe : Heroe) {
       heroes.forEach{
           it.selected = false
       }
       heroe.selected = true
       _uiState.value = HeroesState.HeroeDetail(heroe)
       //_uiState.value = HeroesState.Idle
       _uiStateDetail.value = HeroeDetailState.HeroeDetail(heroe)

        Log.i("HEROES_HERE", "Listado de heroes  VAAVIO" + _uiState.value)
        Log.i("HEROES_DETAILER", "DETALLE" + _uiStateDetail.value)
   }

    fun getDamage(heroe: Heroe) : Heroe {
        heroe.actualLife = (10..60).shuffled().first()

        return heroe
    }

    fun getHealth(heroe: Heroe) : Heroe {
        heroe.actualLife += 20

        return heroe
    }

    /**
     *  ESTADOS A RETORNAR SEGÚN EL STATE
     */
    sealed class HeroesState{
        data class HeroesList(val heroes : List<Heroe>) : HeroesState()
        data class Error(val message : String) : HeroesState()
        data class HeroeDetail(val hero : Heroe) : HeroesState()
        // OBJECTS
        object UpdateList : HeroesState()
        object Idle : HeroesState()
    }


    /**
     *  MOTIVADO A QUE EL DETAIL ESTÁ ENGANCHADO DEL LIST, INVOCAREMOS AL STATE DESDE ACÁ PARA CAPTURAR EL EVENTO
     */
    sealed class HeroeDetailState{
        data class HeroeDetail(val heroe : Heroe) : HeroeDetailState()
        data class HeroeUpdate(val heroe : Heroe) : HeroeDetailState()
        data class Error(val message : String) : HeroeDetailState()
        object Idle : HeroeDetailState()
    }

    companion object {
        const val BASE_URL = "https://dragonball.keepcoding.education"
        const val AUTHORIZATION = "Authorization"
    }
}