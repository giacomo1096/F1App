package com.example.f1app

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.f1app.R
import android.annotation.SuppressLint
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import com.example.f1app.newsAdapter.ViewHolder
import it.sapienza.sportnewsapp.news.News


class newsAdapter(contextFrag: Context, jsonResponses:MutableList<News>) : RecyclerView.Adapter<ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<News> = jsonResponses

    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val news_title: TextView
        //val news_image: TextView
        val news_desc: TextView
        val news_link: TextView

        init {
            news_title = itemView.findViewById(R.id.news_title)
            //news_image = itemView.findViewById(R.id.)
            news_desc = itemView.findViewById(R.id.news_desc)
            news_link = itemView.findViewById(R.id.news_link)

            //Toast.makeText(context, "DEVI FUNGERE :$resp", Toast.LENGTH_LONG).show() //display the response on screen

            }
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.news_title.text = resp.get(i).title
        viewHolder.news_desc.text = resp.get(i).desc
        viewHolder.news_link.text = resp.get(i).link

    }

    override fun getItemCount(): Int {
        return resp.size
    }

}
