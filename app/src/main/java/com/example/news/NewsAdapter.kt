package com.example.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.datamodels.News

class NewsAdapter(): RecyclerView.Adapter<NewsViewHolder>() {
    private val arrayList = ArrayList<News>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.time.text = arrayList[position].publishedAt
        holder.title.text = arrayList[position].title
        holder.information.text = arrayList[position].description
        holder.source.text = arrayList[position].source
        Glide.with(holder.itemView.context).load(arrayList[position].urlImage).placeholder(R.drawable.baseline_image_24).into(holder.image)
    }

    fun updateList(list: ArrayList<News>){
        arrayList.clear()
        arrayList.addAll(list)

        notifyDataSetChanged()
    }

}

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val time : TextView = itemView.findViewById(R.id.time)
    val title: TextView = itemView.findViewById(R.id.title)
    val information: TextView = itemView.findViewById(R.id.information)
    val image: ImageView = itemView.findViewById(R.id.image)
    val source: TextView = itemView.findViewById(R.id.source)
}