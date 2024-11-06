package com.dicoding.asclepius.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.ClassificationEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class ListHistoryAdapter(private val onClickBtnDelete: (ClassificationEntity) -> Unit) :
    ListAdapter<ClassificationEntity, ListHistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val classificationResult = getItem(position)
        holder.binding.tvHistory.text =
            "${classificationResult.category} - ${classificationResult.confidence}"
        holder.binding.ivHistory.setImageURI(Uri.parse(classificationResult.imageUri))

        holder.binding.btnDelete.setOnClickListener {
            onClickBtnDelete(classificationResult)
        }
    }

    class ListViewHolder(var binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClassificationEntity>() {
            override fun areItemsTheSame(
                oldItem: ClassificationEntity,
                newItem: ClassificationEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ClassificationEntity,
                newItem: ClassificationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}