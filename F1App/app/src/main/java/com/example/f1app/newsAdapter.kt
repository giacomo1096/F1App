package com.example.f1app

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.example.f1app.newsAdapter.ViewHolder
import com.squareup.picasso.Picasso

import com.example.f1app.News




class newsAdapter(contextFrag: Context, jsonResponses:MutableList<News>) : RecyclerView.Adapter<ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<News> = jsonResponses

    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val news_title: TextView
        val news_desc: TextView
        val news_link: TextView

        val ivBasicImage: ImageView

        init {
            news_title = itemView.findViewById(R.id.news_title)
            news_desc = itemView.findViewById(R.id.news_desc)
            news_link = itemView.findViewById(R.id.news_link)

            ivBasicImage = itemView.findViewById<View>(R.id.news_img) as ImageView

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
        val imageUri = resp.get(i).image
        Picasso.get().load(imageUri).into(viewHolder.ivBasicImage)
        viewHolder.news_desc.text = resp.get(i).desc
        viewHolder.news_link.text = resp.get(i).link
        viewHolder.news_link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resp.get(i).link))
            context.startActivity(browserIntent)
        }

    }

    override fun getItemCount(): Int {
        return resp.size
    }

}
