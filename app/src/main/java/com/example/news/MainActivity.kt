package com.example.news

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.example.news.auth.AuthenticationActivity
import com.example.news.databinding.ActivityMainBinding
import com.example.news.datamodels.News
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel
        ::class.java]

        val key: String = BuildConfig.KEY
        val adapter = NewsAdapter()

        viewModel.newsList.observe(this, Observer {
            adapter.updateList(it)
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchNews(key, binding.searchNews.text.toString())

        binding.searchButton.setOnClickListener {
            fetchNews(key, binding.searchNews.text.toString())
        }
        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchNews(key: String, query: String) {
        val newUrl = if(query.isEmpty()){
            viewModel.apiUrl+"apiKey="+key
        }
        else{
            viewModel.apiUrl + "q=" + query + "&apiKey=" + key
        }
        val jsonObjectRequest = object: JsonObjectRequest(newUrl, {
            val arrayList = ArrayList<News>()
            val articleArray: JSONArray = it.getJSONArray("articles")
            for (i in 0 until articleArray.length()) {
                val title = articleArray.getJSONObject(i).getString("title")
                val information = articleArray.getJSONObject(i).getString("description")
                val urlToImage = articleArray.getJSONObject(i).getString("urlToImage")
                val time = articleArray.getJSONObject(i).getString("publishedAt")
                val source = articleArray.getJSONObject(i).getJSONObject("source").getString("name")
                arrayList.add(News(time, urlToImage, title, source, information))
            }
            viewModel.newsList.value = arrayList

        }, {

        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        VolleySingleton.getInstance()?.addNetworkRequest(this, jsonObjectRequest)
    }
}