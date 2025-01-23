package com.brunocarvalho.appguiadefilmes.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        //1) Acessar a requisição
        val construtorRequsicao = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${RetrofitService.TOKEN}")  // Adiciona o Bearer Token
            .build()

        //2) Alterar URL ou rota da requisição (Para API  Key na URL)
        //Para API KEY utiliza o anterior apenas até newBuilder corta a parte do addHeader
        /*        val urlAtual = chain.request().url()
                val novaUrl = urlAtual.newBuilder()
                novaUrl.addQueryParameter("api_key", valorDaAPI)

                //3) Configuar nova URL na requisição
                construtorRequsicao.url(novaUrl.build())
                return chain.proceed(construtorRequsicao.build())*/

        return chain.proceed(construtorRequsicao)


    }
}