package com.example.dragonball_api_kc_android.ui.heroes_list

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
import com.bumptech.glide.Glide
import com.example.dragonball_api_kc_android.R
import com.example.dragonball_api_kc_android.databinding.FragmentDetailBinding
import com.example.dragonball_api_kc_android.databinding.HeroeCellBinding
import com.example.dragonball_api_kc_android.model.Heroe
import com.example.dragonball_api_kc_android.view_model.HeroesListViewModel
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private lateinit var detailBinding : FragmentDetailBinding
    private lateinit var cellBinding : HeroeCellBinding

    // implementamos el viewmodel cojunto para capturar los eventos
    private val viewModel : HeroesListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        detailBinding = FragmentDetailBinding.inflate(inflater)
        return detailBinding.root
    }

    // LA VIEWCREATED INICIALIZARÁ LOS OBSERVER'S Y LOS OYENTE'S PARA DISPARAR LOS EVENTOS SEGÚN CADA EVENTO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }


    // los observadores serán los que capturaran la reactividad y dispararán el evento, por ello se debe de definir el estate en ésta función
    // EL OBSERVADOR DEBE DE APUNTAR A LA SEALED CLASS
    private fun setObservers()  {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.detailState.collect{
                when(it) {
                    is HeroesListViewModel.HeroeDetailState.HeroeDetail -> {
                        Log.i("DETAIL", "EL HEROE DETALLE ES ${it.heroe}")
                        printDetail(it.heroe)
                    }
                    is HeroesListViewModel.HeroeDetailState.HeroUpdate -> {
                        printDetail(it.heroe)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun printDetail(heroe: Heroe) {
        with(detailBinding){
            tvDetailTitle.text = heroe.name
            tvLife.text = heroe.maxLife.toString()
            tvDetailheroActualLife.text = heroe.actualLife.toString()
            sbLive.progress = heroe.actualLife
            Glide
                .with(root)
                .load(heroe.photo)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(detailphotoHeroe)


            btCurarse.setOnClickListener {
                var her = viewModel.getHealth(heroe)
                sbLive.progress = her.actualLife
                tvDetailheroActualLife.text = sbLive.progress.toString()
            }

            btDamage.setOnClickListener {
                var her = viewModel.getDamage(heroe)
                sbLive.progress = her.actualLife
                tvDetailheroActualLife.text = sbLive.progress.toString()

                if(sbLive.progress == 0) {
                    detailphotoHeroe.alpha = 0F
                    parentFragmentManager.popBackStack()
                }else {
                    detailphotoHeroe.alpha = 1F
                }
            }
        }
    }


}