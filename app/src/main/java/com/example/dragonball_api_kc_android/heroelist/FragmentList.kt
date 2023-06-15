package com.example.dragonball_api_kc_android.heroelist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragonball_api_kc_android.databinding.FragmentListBinding
import com.example.dragonball_api_kc_android.heroelist.model.Heroe
import com.example.dragonball_api_kc_android.persistence.UserDetails
import kotlinx.coroutines.launch
import java.lang.Exception

class FragmentList : Fragment(), AdapterCallback {

    private lateinit var fragmentListBinding : FragmentListBinding
    private val viewModel : HeroesListViewModel by viewModels()

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

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                when(it) {
                    is HeroesListViewModel.HeroesState.HeroesList -> {
                        showHeroes(it.heroes)
                        Log.i("LISTA_HEROES", it.heroes.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showHeroes(heroes : List<Heroe>) {
        adapter.updateList(heroes)
    }

    private fun loadHeroes() {
        //val heroe : Array<Heroe> = Gson().fromJson(this, Array<Heroe>::class.java)
        activity?.getPreferences(Context.MODE_PRIVATE)?.let {
            try {
                UserDetails.callToken(requireContext())?.let {
                    viewModel.downloadHeroes(it)
                }
            }
            catch (_: Exception) {
                Log.i("ERROR_TOKEN", "Error en el token ")
            }
        }
    }
}