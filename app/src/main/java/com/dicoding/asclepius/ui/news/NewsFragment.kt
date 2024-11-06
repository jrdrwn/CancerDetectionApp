package com.dicoding.asclepius.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.utils.Result

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireActivity().application
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getNewsData()
        }

        viewModel.news.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val listArticlesItem = result.data
                        binding.rvNews.layoutManager = LinearLayoutManager(requireActivity())
                        val adapter = NewsAdapter(listArticlesItem)
                        binding.rvNews.adapter = adapter
                        binding.errorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnTryAgain.setOnClickListener {
            viewModel.getNewsData()
            binding.errorPage.visibility = View.GONE
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}