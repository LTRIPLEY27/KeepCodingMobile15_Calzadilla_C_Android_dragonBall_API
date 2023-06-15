package com.example.dragonball_api_kc_android.persistence

import android.content.Context

/**
 * CLASE QUE ALMACENARÁ VARIABLES EN ENTORNO DE PERSISTENCIA MEDIANTE EL SHAREDPREFERENCES
 */
class UserDetails {

    companion object {
        private val preferences = "UserDetails"
        private var TOKEN = "TOKEN_USER"



        // APLICAMOS EL EDIT PARA ALMACENAR INFORMACIÓN EN EL CONTEXTO DE LA SHARED PREFERENCES
        fun checkToken(token : String, context: Context) {
            with(context.getSharedPreferences(preferences, Context.MODE_PRIVATE).edit()) {
                putString(TOKEN, token)
                apply()
            }
        }

        fun callToken(context: Context) = context.getSharedPreferences(preferences, Context.MODE_PRIVATE)
            .getString(TOKEN, "")
    }
}