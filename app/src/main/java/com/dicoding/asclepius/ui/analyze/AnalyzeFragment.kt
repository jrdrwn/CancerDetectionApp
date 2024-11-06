package com.dicoding.asclepius.ui.analyze

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.ClassificationResult
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class AnalyzeFragment : Fragment() {
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    private var _binding: FragmentAnalyzeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val analyzeViewModel =
            ViewModelProvider(this).get(AnalyzeViewModel::class.java)

        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        return root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {

            val ucropintent = UCrop.of(
                uri, Uri.fromFile(
                    requireContext().cacheDir.resolve(
                        "${
                            System.currentTimeMillis()
                        }.jpg"
                    )
                )
            )
                .getIntent(requireContext())
            cropActivityResultLauncher.launch(ucropintent)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val cropActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val resultUri = UCrop.getOutput(result.data!!)
                println(resultUri)
                if (resultUri != null) {
                    resetPreview()
                    currentImageUri = resultUri
                    showImage()
                } else {
                    showToast("Crop failed")
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(result.data!!)
                showToast("Crop error: ${cropError?.message}")
        }
    }

    private fun resetPreview() {
        binding.previewImageView.setImageURI(null)
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        binding.progressIndicator.visibility = View.VISIBLE
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    activity?.runOnUiThread {
                        results?.let { it ->
                            binding.progressIndicator.visibility = View.GONE
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                moveToResult(
                                    ClassificationResult(
                                        it[0].categories[0].label,
                                        NumberFormat.getPercentInstance().format(it[0].categories[0].score),
                                        currentImageUri!!
                                    )
                                )
                            } else {
                                showToast("No prediction result")
                            }
                        }
                    }
                }
            }
        )
        if (currentImageUri == null) {
            binding.progressIndicator.visibility = View.GONE
            showToast("No image selected")
            return
        }
        imageClassifierHelper.classifyStaticImage(currentImageUri!!)
    }

    private fun moveToResult(result: ClassificationResult) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}