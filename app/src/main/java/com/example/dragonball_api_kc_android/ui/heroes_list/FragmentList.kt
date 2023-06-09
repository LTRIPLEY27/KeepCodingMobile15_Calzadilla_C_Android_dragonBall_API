package com.example.dragonball_api_kc_android.ui.heroes_list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragonball_api_kc_android.databinding.FragmentDetailBinding
import com.example.dragonball_api_kc_android.databinding.FragmentListBinding
import com.example.dragonball_api_kc_android.heroelist.AdapterCallback
import com.example.dragonball_api_kc_android.heroelist.HeroeListAdapter
import com.example.dragonball_api_kc_android.model.Heroe
import com.example.dragonball_api_kc_android.persistence.UserDetails
import com.example.dragonball_api_kc_android.view_model.HeroesListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.Exception

class FragmentList : Fragment(), AdapterCallback {

    private lateinit var fragmentListBinding : FragmentListBinding
    private lateinit var fragmentDetail: FragmentDetailBinding

    /**
     * EL VIEW MODELS SE HA DE DECLARAR ENCADENADO A LA ACTIVITY PARA QUE TRASLADE LOS DATOS DE UN FRAGMENT A OTRO U OTRO CONTEXTO, YA QUE CASO CONTRARIO RETORNARÁ NULL ETERNAMENTE
     * PARA ENGANCHAR EL VIEWMODEL A LA ACTIVITY :  by activityViewModels()
    */
    private val viewModel : HeroesListViewModel by activityViewModels()

    // recogemos el valor del contrato del adapter con el héroe específico
    private val adapter = HeroeListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentListBinding = FragmentListBinding.inflate(inflater)
        return fragmentListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        loadHeroes()
    }
    override fun getHeroe(hero: Heroe) {
        viewModel.getHero(hero)
    }

    // DEFINIMOS EL ADAPTER A ENGANCHAR
    private fun setAdapter() {
        with(fragmentListBinding) {
            rvHeroes.layoutManager = LinearLayoutManager(requireContext())
            rvHeroes.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                when(it) {
                    is HeroesListViewModel.HeroesState.HeroesList -> {
                        showHeroes(it.heroes)
                        chargeOnLocal(it.heroes)
                        Log.i("LISTA_HEROES", it.heroes.toString())
                    }
                    is HeroesListViewModel.HeroesState.HeroeDetail-> {
                        getDetail(it.hero)
                        Log.i("HEROE_DETAIL_CALLA", " $it.hero")
                    }
                    is HeroesListViewModel.HeroesState.UpdateList -> {
                        adapter.notifyDataSetChanged()
                    }
                    else -> {}
                }
            }
        }
    }


    private fun getDetail(heroe : Heroe) {
        Log.i("HEROE_DETAIL", " $heroe")
        (activity as HeroesActivity).getDetail()
    }

    private fun showHeroes(heroes : List<Heroe>) {
        heroes.forEach{
            if(it.actualLife < 0 ){
                it.selected = false
            }
        }
        adapter.updateList(heroes)
    }

    // ALMACENA EN LOCAL LA LISTA  RECIBIDA DESERIALIZANDO EL JSON
    private fun chargeOnLocal(heroes : List<Heroe>){
        activity?.getPreferences(Context.MODE_PRIVATE)?.let {
            it.edit()
                .putString(HEROES_ON_LOCAL, Gson().toJson(heroes))
            it.edit()
        }
    }

    private fun loadHeroes() {
        activity?.getPreferences(Context.MODE_PRIVATE)?.let {
            try {
                val response = it.getString(HEROES_ON_LOCAL, "")
                val getHeroes : Array<Heroe> = Gson().fromJson(response, Array<Heroe>::class.java)
                viewModel.setHeros(getHeroes.toList())

            }
            catch (_: Exception) {
                UserDetails.callToken(requireContext())?.let { withToken ->
                    viewModel.downloadHeroes(withToken)
                }
                Log.i("ERROR_TOKEN", "Error en el token ")
            }
        }
    }

    companion object {
        const val HEROES_ON_LOCAL = "HEROES_LOCAL"
    }
}