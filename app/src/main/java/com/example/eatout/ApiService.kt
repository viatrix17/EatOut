package com.example.eatout

import com.example.eatout.model.Post
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.http.GET
import retrofit2.http.Query
class ApiService {
    companion object {

    public fun GET(url: String, callback: Callback): Call {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("User-Agent", "MojaAplikacjaPoznan/1.0 (kontakt@twojadomena.pl)")
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
    }
}