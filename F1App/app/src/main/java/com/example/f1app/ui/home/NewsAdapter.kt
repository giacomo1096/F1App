package com.example.f1app.ui.home

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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.ui.home.newsAdapter.ViewHolder
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONObject
import com.example.f1app.*
import com.example.f1app.ui.favAndShake.favoritesAdapter
import kotlinx.android.synthetic.main.fragment_favandshake.*
import org.json.JSONException
import java.security.MessageDigest


class newsAdapter(contextFrag: Context, jsonResponses:MutableList<News>) : RecyclerView.Adapter<ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<News> = jsonResponses

    private val urlCreate = URL_PYTHONANYWHERE + "createfavorite"
    private val urlDelete = URL_PYTHONANYWHERE + "deletefavorite"
    val favId = getFavoriteId()

    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val news_title: TextView
        val news_desc: TextView
        val news_link: TextView
        val news_pref: ImageView
        val news_no_pref: ImageView
        val ivBasicImage: ImageView

        init {
            news_title = itemView.findViewById(R.id.news_title)
            news_desc = itemView.findViewById(R.id.news_desc)
            news_link = itemView.findViewById(R.id.news_link)
            news_no_pref = itemView.findViewById(R.id.empty_star)
            news_pref = itemView.findViewById(R.id.star)

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
        if(favId.contains(resp.get(i).id)){
            viewHolder.news_pref.visibility = View.VISIBLE
            viewHolder.news_no_pref.visibility = View.GONE
        }
        viewHolder.news_title.text = resp.get(i).title.substring(1)
        val imageUri = resp.get(i).image
        Picasso.get().load(imageUri).into(viewHolder.ivBasicImage)
        viewHolder.news_desc.text = resp.get(i).desc
        viewHolder.news_link.text = resp.get(i).link
        viewHolder.news_link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resp.get(i).link))
            context.startActivity(browserIntent)
        }

        viewHolder.news_no_pref.setOnClickListener {
            viewHolder.news_pref.visibility = View.VISIBLE
            viewHolder.news_no_pref.visibility = View.GONE

            val requestQueue = Volley.newRequestQueue(context)
            val newsJSON = JSONObject()
            newsJSON.put("id", resp.get(i).id)
            newsJSON.put("webTitle", resp.get(i).title)
            newsJSON.put("webImage", resp.get(i).image)
            newsJSON.put("webDesc", resp.get(i).desc)
            newsJSON.put("webUrl", resp.get(i).link)
            val jsonobj = JSONObject()
            jsonobj.put("news", newsJSON)
            jsonobj.put("userId", userId)
            val request = JsonObjectRequest(Request.Method.POST,urlCreate,jsonobj,
                { response ->
                    // Process the json
                    try {
                        //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    }catch (e:Exception){
                        Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                    }

                }, {
                    // Error in request
                    //Toast.makeText(context, "Volley error: $it", Toast.LENGTH_LONG).show()
                })
            requestQueue.add(request)
        }

        viewHolder.news_pref.setOnClickListener {
            viewHolder.news_pref.visibility = View.GONE
            viewHolder.news_no_pref.visibility = View.VISIBLE
            val requestQueue = Volley.newRequestQueue(context)
            val jsonobj = JSONObject()
            jsonobj.put("newsId", resp.get(i).id)
            jsonobj.put("userId", userId)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlDelete, jsonobj,
                { response ->
                    try {
                        //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    }catch (e:Exception){
                        Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                    }
                }) { error ->  //Toast.makeText(context, "Volley error: $it", Toast.LENGTH_LONG).show()
            }

            requestQueue.add(jsonObjectRequest)
        }

    }

    override fun getItemCount(): Int {
        return resp.size
    }

    private fun getFavoriteId(): MutableList<String> {
        val url = URL_PYTHONANYWHERE + "favorites?userId="+userId
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        //Create new String request
        var prefe : MutableList<String> = mutableListOf<String>()
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val resultArray = response.getJSONArray("favorites")
                    for (i in 0 until resultArray.length()) {
                        val jo = resultArray.getJSONObject(i)
                        val news_id = jo.getString("id")
                        //Toast.makeText(context, "Response: $news_id list  $prefe", Toast.LENGTH_LONG).show()
                        prefe.add(news_id)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }

        //add string request to request queue
        requestQueue.add(stringRequest)
        return prefe
    }


}
