package rivy.assignment.foodblogapp.REST

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofilHandler {

    private val URL = "https://my-json-server.typicode.com/guljar-rivi/server/db/"
    private var retrofit: Retrofit? = null

    val retrofitClient : API get() {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return this.retrofit!!.create(API::class.java)
    }

}