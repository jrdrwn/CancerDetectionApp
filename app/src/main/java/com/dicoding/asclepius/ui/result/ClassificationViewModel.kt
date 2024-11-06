package com.dicoding.asclepius.ui.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.ClassificationEntity
import com.dicoding.asclepius.repository.ClassificationRepository

class ClassificationViewModel(application: Application) : ViewModel() {
    private val mClassificationRepository: ClassificationRepository =
        ClassificationRepository(application)

    fun getAllClassification(): LiveData<List<ClassificationEntity>> =
        mClassificationRepository.getAllClassification()

    fun insert(classificationEntity: ClassificationEntity) {
        mClassificationRepository.insert(classificationEntity)
    }

    fun delete(classificationEntity: ClassificationEntity) {
        mClassificationRepository.delete(classificationEntity)
    }
}