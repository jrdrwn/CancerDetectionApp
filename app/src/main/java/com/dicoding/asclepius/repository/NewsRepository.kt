package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiService
import com.dicoding.asclepius.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository private constructor(private val apiService: ApiService) {
    private val result = MediatorLiveData<Result<List<ArticlesItem>>>()

    fun getNews(): LiveData<Result<List<ArticlesItem>>> {
        result.value = Result.Loading
        val client = apiService.getNews()
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!.articles)
                } else {
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(apiService: ApiService): NewsRepository {
            return instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService).also { instance = it }
            }
        }
    }
}