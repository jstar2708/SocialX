package com.example.news

import android.content.Context
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(){
    companion object{
        private var singleInstance : VolleySingleton? = null
        fun getInstance() : VolleySingleton?{
            if(singleInstance == null){
                singleInstance = VolleySingleton()
            }
            return singleInstance
        }
    }

    fun addNetworkRequest(context: Context, jsonObjectRequest: JsonObjectRequest){
        val queue = Volley.newRequestQueue(context.applicationContext)
        queue.add(jsonObjectRequest)
    }

}