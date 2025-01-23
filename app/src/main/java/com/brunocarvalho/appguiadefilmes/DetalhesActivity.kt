package com.brunocarvalho.appguiadefilmes

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brunocarvalho.appguiadefilmes.api.RetrofitService
import com.brunocarvalho.appguiadefilmes.databinding.ActivityDetalhesBinding
import com.brunocarvalho.appguiadefilmes.model.Filme
import com.squareup.picasso.Picasso

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate( layoutInflater )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        val bundle = intent.extras

        if(bundle != null){

            val filme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("filme", Filme::class.java)
            } else {
                bundle.getParcelable("filme") as? Filme
            }

            if(filme != null){
                binding.textFilmeTitulo.text = filme.title
                binding.textDescricao.text =  filme.overview
                binding.textMediaVotos.text = "Média: " + filme.vote_average

                val nomeFilme = filme.backdrop_path
                val tamanhoFilme = "w780"
                val urlFilme = RetrofitService.BASE_URL_IMAGEM + tamanhoFilme + nomeFilme

                Picasso.get()
                    .load(urlFilme)
                    .into(binding.imgPoster)
            }

        }else{

        }

    }
}