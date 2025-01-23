package com.brunocarvalho.appguiadefilmes.api

import com.brunocarvalho.appguiadefilmes.model.FilmeRecente
import com.brunocarvalho.appguiadefilmes.model.FilmeResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeAPI {

    @GET("movie/latest")
    //@Headers("Authorization: Bearer ${RetrofitService.TOKEN}")
    suspend fun recuperarFilmeRecente() : Response<FilmeRecente>

    @GET("movie/popular")
    //@Headers("Authorization: Bearer ${RetrofitService.TOKEN}")
    suspend fun recuperarFilmesPopulares(
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int
    ): Response<FilmeResposta>
}