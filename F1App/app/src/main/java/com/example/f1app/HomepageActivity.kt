package com.example.f1app

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.f1app.databinding.ActivityHomepageBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request

import org.json.JSONException

import org.json.JSONObject

import org.json.JSONArray

import com.android.volley.toolbox.StringRequest

import com.android.volley.toolbox.Volley

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.fragment_history.*


class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    val news_list: MutableList<News> = mutableListOf<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_activity_homepage)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favAndShake, R.id.navigation_history, R.id.navigation_home, R.id.navigation_lastRace, R.id.navigation_teamsAndDrivers

            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        prepareData()

        //val thread = SensorThread(this)
        //thread.start()
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