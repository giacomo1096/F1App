package com.example.f1app.ui.favAndShake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.News
import com.example.f1app.R
import com.example.f1app.SharedViewModel
import com.example.f1app.databinding.ActivityHomepageBinding
import com.example.f1app.newsAdapter
import com.example.f1app.ui.history.circuitAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONArray
import org.json.JSONException


class favAndShakeFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    private val url = "http://192.168.1.139:8000/circuits"

    val news_list: MutableList<News> = mutableListOf<News>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_favandshake, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("list")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id = jsonObject.getString("id")
                        val circuit_name = jsonObject.getString("circuit")
                        val item = mapOf(id to circuit_name)
                        jsonResponses.add(item)
                    }
                    //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                    rvcircuits.apply {
                        // set a LinearLayoutManager to handle Android
                        // RecyclerView behavior
                        layoutManager = LinearLayoutManager(activity)
                        // set the custom adapter to the RecyclerView
                        adapter = circuitAdapter(context, jsonResponses)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)
    }

    private fun prepareData() {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(this)
        //Create new String request

        val stringRequest = StringRequest(
            Request.Method.GET, "https://skysportsapi.herokuapp.com/sky/getnews/f1/v1.0/",
            { response: String? ->
                try {
                    val resultArray = JSONArray(response)
                    for (i in 0 until resultArray.length()) {
                        val jo = resultArray.getJSONObject(i)
                        val news_title = jo.getString("title")
                        val news_image = jo.getString("imgsrc")
                        val news_desc = jo.getString("shortdesc")
                        val news_link = jo.getString("link")

                        //Toast.makeText(this, "TEST news-title: $news_title desc: $news_desc link: $news_link", Toast.LENGTH_LONG).show() //display the response on screen

                        val news_item = News(news_title, news_image, news_desc, news_link)

                        news_list.add(news_item)
                    }

                    rvnews.apply {
                        // set a LinearLayoutManager to handle Android
                        // RecyclerView behavior
                        layoutManager = LinearLayoutManager(context)
                        // set the custom adapter to the RecyclerView
                        adapter = newsAdapter(context, news_list)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
        }

        //add string request to request queue
        requestQueue.add(stringRequest)
    }



}