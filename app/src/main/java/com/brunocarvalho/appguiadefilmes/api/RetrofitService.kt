package com.brunocarvalho.appguiadefilmes.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
    const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlYzE3ODA0ODc1ZmY3M2RkMjIwMWJiMGExOTdkNWQzYyIsIm5iZiI6MTczNTQ4MTc1OS4yNTEwMDAyLCJzdWIiOiI2NzcxNTk5ZjVmMWM0ZmE0NzM2MTU5YzkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.DaikfTeyRlAu3wL4fK1LdQcw-fyIzH3COdsqaLf8zRM"

    private val okHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS) //Salvando algo na API  (Essa configuração diz que o tempo para escrita numa API é no máximo de 30 segundos)
        .readTimeout(20, TimeUnit.SECONDS) //O mesmo que o de cima, mas para leitura
        //.connectTimeout(20, TimeUnit.SECONDS) //Tempo máximo da conexão com a API
        .addInterceptor(AuthInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl( BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .client(okHttpClient)
        .build()

    val filmeAPI = retrofit.create( FilmeAPI::class.java )

    fun <T> recuperarApi(classe: Class<T>): T{
        return retrofit.create(classe)
    }
}