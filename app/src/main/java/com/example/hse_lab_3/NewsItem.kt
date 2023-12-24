package com.example.hse_lab_3

import com.google.gson.annotations.SerializedName
import java.net.URL

data class NewsItem (
    val articleId: String,
    val title: String,
    val link: URL,
    val keywords: List<String>,
    val creator: List<String>,
    @SerializedName("video_url")
    val videoURL: URL,
    val description: String,
    val content: String,
    val pubDate: String,
    @SerializedName("image_url")
    val imageURL: URL,
    @SerializedName("source_id")
    val sourceID: String,
    @SerializedName("source_priority")
    val sourcePriority: Int,
    val country: List<String>,
    val category: List<String>,
    val language: String
)
