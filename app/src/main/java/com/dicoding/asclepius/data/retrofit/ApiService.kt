package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("v2/top-headlines?q=cancer&category=health&language=en&apiKey=1574d30aeec84efbad9a7005bde25755")
    fun getNews(): Call<NewsResponse>
}