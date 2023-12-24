package com.example.hse_lab_3

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import com.bumptech.glide.Glide

class Adapter(private val newsImages: List<URL>,
              private val newsTitles: List<String>,
              private val newsDescriptions: List<String>,
              private val newsSource: List<URL>
) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)

        val viewHolder = NewsViewHolder(newsView)

        newsView.setOnClickListener {
            val position = viewHolder.adapterPosition

            if (position != RecyclerView.NO_POSITION) {
                val sourceUrl = newsSource[position]
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl.toString()))
                it.context.startActivity(intent)
            }
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        holder.newsTitle.text = newsTitles[position]
        holder.newsDescription.text = newsDescriptions[position]

        Glide.with(holder.newsImage)
            .load(newsImages[position])
            .into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        return newsTitles.size
    }
}

