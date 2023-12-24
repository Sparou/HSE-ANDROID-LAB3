package com.example.hse_lab_3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URL

class MainActivity: AppCompatActivity() {

    private lateinit var keyWordEditText: EditText;
    private lateinit var searchButton: Button;

    private var apiKey = "YOUR newsdata.io API KEY"

    private var newsImages: MutableList<URL> = ArrayList()
    private var newsTitles: MutableList<String> = ArrayList()
    private var newsDescription: MutableList<String> = ArrayList()
    private var newsSource: MutableList<URL> = ArrayList()

    private var currentJob: Job? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: Adapter
    private lateinit var newsApiService: NewsApiService

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    interface NewsApiService {
        @GET("news?")
        suspend fun getNews(@Query("apikey") apiKey: String, @Query("q") query: String): NewsAPI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)

        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsdata.io/api/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            newsApiService = retrofit.create(NewsApiService::class.java)
            Toast.makeText(applicationContext, "Successful initialize", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Initialize failed", Toast.LENGTH_SHORT).show()
        }

        keyWordEditText = findViewById(R.id.editTextKeyword)
        searchButton = findViewById(R.id.buttonSearch)
        recyclerView = findViewById(R.id.recyclerViewNews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = Adapter(newsImages, newsTitles, newsDescription, newsSource)

        recyclerView.adapter = newsAdapter

        searchButton.setOnClickListener(View.OnClickListener {
            val keyword = keyWordEditText.text.toString()
            if (keyword.isNotEmpty()) {

                currentJob = CoroutineScope(Dispatchers.Main).launch {
                    sendRequest(keyword)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun sendRequest(request: String){
        try {
            val news = withContext(Dispatchers.IO){
                newsApiService.getNews(apiKey, request.toString())
            }

            for(article in news.results){
                newsImages.add(0, article.imageURL)
                newsAdapter?.notifyItemInserted(newsImages.size - 1);
                newsTitles.add(0, article.title)
                newsAdapter?.notifyItemInserted(newsTitles.size - 1);
                newsDescription.add(0,article.description)
                newsAdapter?.notifyItemInserted(newsDescription.size - 1);
                newsSource.add(0,article.link)
                newsAdapter?.notifyItemInserted(newsSource.size - 1);
            }

            newsAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(0)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext,"FAIL", Toast.LENGTH_SHORT).show()
        }
    }
}