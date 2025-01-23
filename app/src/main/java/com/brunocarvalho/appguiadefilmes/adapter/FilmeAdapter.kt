package com.brunocarvalho.appguiadefilmes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brunocarvalho.appguiadefilmes.api.RetrofitService
import com.brunocarvalho.appguiadefilmes.databinding.ItemFilmeBinding
import com.brunocarvalho.appguiadefilmes.model.Filme
import com.squareup.picasso.Picasso

class FilmeAdapter(
    val onClick: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    private var listaFilmes = mutableListOf<Filme>()

    fun adicionarLista(lista: List<Filme>){
        this.listaFilmes.addAll(lista)
        notifyDataSetChanged()
    }

    inner class FilmeViewHolder(val binding: ItemFilmeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: Filme){
            val nomeFilme = filme.poster_path
            val tamanhoFilme = "w500"
            val urlFilme = RetrofitService.BASE_URL_IMAGEM + tamanhoFilme + nomeFilme

            Picasso.get()
                .load(urlFilme)
                .into(binding.imgItemFilme)

            binding.textTitulo.text = filme.title

            binding.clItem.setOnClickListener {
                onClick(filme)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemFilmeBinding.inflate(layoutInflater, parent, false)
        return  FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = listaFilmes[position]
        holder.bind(filme)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }


}