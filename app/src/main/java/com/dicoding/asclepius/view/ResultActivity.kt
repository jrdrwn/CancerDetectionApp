package com.dicoding.asclepius.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val result = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ClassificationResult>(EXTRA_RESULT, ClassificationResult::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ClassificationResult>(EXTRA_RESULT)
        }

        binding.resultImage.setImageURI(result?.imageUri)
        binding.resultText.text = "${result?.category} ${result?.confidence}"
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }


}