package cl.duoc.tecmundo.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Debe terminar siempre con "/"
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:nWrPWvIB/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}