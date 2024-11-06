package com.dicoding.asclepius.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ListHistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.ui.result.ClassificationViewModel
import java.io.File

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var classificationViewModel: ClassificationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classificationViewModel.getAllClassification().observe(viewLifecycleOwner) {

            binding.nothingData.isVisible = it.isEmpty()
            binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ListHistoryAdapter { entity ->
                val file = File(entity.imageUri)
                file.delete()
                classificationViewModel.delete(entity)
            }
            adapter.submitList(it)
            binding.rvHistory.adapter = adapter

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        classificationViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity().application)
        ).get(ClassificationViewModel::class.java)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}