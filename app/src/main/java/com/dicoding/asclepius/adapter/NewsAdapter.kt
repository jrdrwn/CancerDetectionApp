package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class NewsAdapter(private val listArticlesItem: List<ArticlesItem?>) :
    RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listArticlesItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val articlesItem = listArticlesItem[position]
        Glide.with(holder.itemView.context)
            .load(articlesItem?.urlToImage)
            .into(holder.binding.ivNews)
        holder.binding.tvSource.text = articlesItem?.source?.name
        holder.binding.tvTitle.text = articlesItem?.title
        holder.binding.tvDescription.text = articlesItem?.description
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articlesItem?.url))
            holder.itemView.context.startActivity(intent)
        }
    }
}