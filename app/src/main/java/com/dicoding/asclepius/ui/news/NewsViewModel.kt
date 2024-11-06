package com.dicoding.asclepius.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.repository.NewsRepository
import com.dicoding.asclepius.utils.Result

class NewsViewModel(private val mNewsRepository: NewsRepository) : ViewModel() {
    lateinit var news: LiveData<Result<List<ArticlesItem>>>

    fun getNewsData(): LiveData<Result<List<ArticlesItem>>> {
        news = mNewsRepository.getNews()
        return news
    }
}
