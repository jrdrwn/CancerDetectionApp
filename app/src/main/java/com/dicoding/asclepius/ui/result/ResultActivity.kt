package com.dicoding.asclepius.ui.result

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.ClassificationEntity
import com.dicoding.asclepius.data.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    private lateinit var classificationViewModel: ClassificationViewModel

    private lateinit var result: ClassificationResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        result = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ClassificationResult>(EXTRA_RESULT, ClassificationResult::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ClassificationResult>(EXTRA_RESULT)
        }!!

        supportActionBar?.apply {
            title = "Analysis Result"
            setDisplayHomeAsUpEnabled(true)
        }

        classificationViewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(
                ClassificationViewModel::class.java
            )

        binding.resultImage.setImageURI(result.imageUri)
        binding.resultText.text = "${result.category} ${result.confidence}"
        binding.btnSave.setOnClickListener { saveToHistory() }

        classificationViewModel.getAllClassification().observe(this) {
            if (it != null) {
                for (classification in it) {
                    println(classification.category)
                    println(classification.confidence)
                    println(classification.imageUri)
                }
            }
        }
    }

    private fun saveToHistory() {
        this.openFileOutput("${timeStamp}.jpg", Context.MODE_PRIVATE).use { output ->
            val inputStream = contentResolver.openInputStream(
                intent.getParcelableExtra<ClassificationResult>(EXTRA_RESULT)?.imageUri as Uri
            ) as InputStream
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
            inputStream.close()
        }

        val imageUri = Uri.fromFile(File(filesDir, "${timeStamp}.jpg"))
        Toast.makeText(this, "Image saved to $imageUri", Toast.LENGTH_SHORT).show()
        classificationViewModel.insert(
            ClassificationEntity(
                category = result.category,
                confidence = result.confidence,
                imageUri = imageUri.toString()
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }


}