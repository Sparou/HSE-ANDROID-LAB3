package com.example.hse_lab_3

data class NewsAPI (
    val status: String,
    val totalResults: Int,
    val results: List<NewsItem>,
    val nextPage: ULong
)