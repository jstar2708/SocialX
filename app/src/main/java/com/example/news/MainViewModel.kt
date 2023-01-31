package com.example.news

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.news.datamodels.News
import org.json.JSONArray

class MainViewModel: ViewModel() {

    val apiUrl = "https://newsapi.org/v2/top-headlines?country=in&"
    var newsList: MutableLiveData<ArrayList<News>> = MutableLiveData()
}