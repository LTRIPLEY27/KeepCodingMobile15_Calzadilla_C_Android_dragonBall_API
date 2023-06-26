package com.example.dragonball_api_kc_android.heroelist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dragonball_api_kc_android.R
import com.example.dragonball_api_kc_android.databinding.HeroeCellBinding
import com.example.dragonball_api_kc_android.model.Heroe

interface AdapterCallback {
    fun getHeroe(hero : Heroe)
}

class HeroeListAdapter(private val callback: AdapterCallback) : RecyclerView.Adapter<HeroeListAdapter.MainViewHolder>() {

    private var heroesList = listOf<Heroe>()

    inner class MainViewHolder(private val binding : HeroeCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun take(hero : Heroe) {
            with(binding) {
                heroName.text = hero.name
                tvheroInitialLife.text = hero.actualLife.toString()
                tvheroActuallLife.text = hero.actualLife.toString()
                tvDescription.text = hero.description
                Glide
                    .with(root)
                    .load(hero.photo)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(photoHeroe)

                // CAPTURA DEL HEROE
                root.setOnClickListener {
                    callback.getHeroe(hero)
                    Log.i("HEROE_CATCH", "EL HEROE ES $hero")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            HeroeCellBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return heroesList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.take(heroesList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Heroe>) {
        heroesList = list
        notifyDataSetChanged()
    }
}