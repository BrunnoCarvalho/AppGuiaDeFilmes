package com.brunocarvalho.appguiadefilmes

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.brunocarvalho.appguiadefilmes.adapter.FilmeAdapter
import com.brunocarvalho.appguiadefilmes.api.FilmeAPI
import com.brunocarvalho.appguiadefilmes.api.RetrofitService
import com.brunocarvalho.appguiadefilmes.databinding.ActivityMainBinding
import com.brunocarvalho.appguiadefilmes.model.FilmeResposta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var paginaAtual = 1
    private var totalPaginasFilmesPopulares: Int? = null
    private var jobFilmesPopulares: Job? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var filmeAdapter: FilmeAdapter

    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    private val filmeAPI by lazy {
        //RetrofitService.filmeAPI
        RetrofitService.recuperarApi(FilmeAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#f70000")))
        inicializarViews()
        recuperarFilmesPopulares()
    }

    override fun onStop() {
        super.onStop()
        jobFilmesPopulares?.cancel()
    }


    private fun inicializarViews() {

        filmeAdapter = FilmeAdapter { filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter
        /*gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)*/
        gridLayoutManager = GridLayoutManager(this, 2)
        binding.rvPopulares.layoutManager = gridLayoutManager
        binding.rvPopulares.addOnScrollListener( object : OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val podeDescerVerticalmente = recyclerView.canScrollVertically(1)
                //1) Chegar ao final da lista
                if(!podeDescerVerticalmente){ //não puder descer mais, carregamos mais itens
                    //carregar mais 20 itens
                    recuperarFilmesPopularesProximaPagina()
                }
            }
        })
    }

    private fun recuperarFilmesPopularesProximaPagina() {
        if(paginaAtual < totalPaginasFilmesPopulares!!){
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    private fun recuperarFilmesPopulares(pagina: Int = 1) {

        if(pagina == 1 && paginaAtual != 1){
            return
        }

        jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch{
            var resposta: Response<FilmeResposta>? = null

            try{
                resposta = filmeAPI.recuperarFilmesPopulares(page = pagina)
            }catch(e: Exception){
                exibirMensagem("Erro ao fazer a requisição")
            }

            if(resposta != null){

                if(resposta.isSuccessful){
                    var numero = 0
                    val filmeResposta = resposta.body()
                    val listaFilmes = filmeResposta?.filmes
                    totalPaginasFilmesPopulares = filmeResposta?.total_pages

                    if(listaFilmes != null){
                        Log.i("filmes_api", "listaFilmes: ")
                        listaFilmes.forEach{ filme ->
                            numero++
                            Log.i("filmes_api", "$numero Titulo: ${filme.title}")
                        }
                        withContext(Dispatchers.Main){
                            filmeAdapter.adicionarLista(listaFilmes)
                        }
                    }

                }else{
                    exibirMensagem("Não foi possível recuperar o filme recente CÓDIGO: ${resposta.code()}")
                }

            }
            else{
                exibirMensagem("Não foi possível fazer a requisição")
            }

        }

    }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(applicationContext, mensagem, Toast.LENGTH_SHORT).show()
    }
}