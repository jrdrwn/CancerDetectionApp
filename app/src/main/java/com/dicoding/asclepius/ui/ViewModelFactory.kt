package com.dicoding.asclepius.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.repository.NewsRepository
import com.dicoding.asclepius.ui.news.NewsViewModel
import com.dicoding.asclepius.ui.result.ClassificationViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE =
                        ViewModelFactory(application, Injection.provideNewsRepository(application))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassificationViewModel::class.java)) {
            return ClassificationViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
