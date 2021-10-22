package com.example.f1app.ui.favAndShake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.ui.home.News
import com.example.f1app.R
import com.example.f1app.URL_PYTHONANYWHERE
import com.example.f1app.userId
import kotlinx.android.synthetic.main.fragment_favandshake.*
import org.json.JSONException


class favAndShakeFragment : Fragment() {
    //userId va reso dinamico
    private val url = URL_PYTHONANYWHERE + "favorites?userId="+userId

    val news_list: MutableList<News> = mutableListOf<News>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_favandshake, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        prepareData()
    }

    private fun prepareData() {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        //Create new String request

        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val resultArray = response.getJSONArray("favorites")
                    for (i in 0 until resultArray.length()) {
                        val jo = resultArray.getJSONObject(i)
                        val news_id = jo.getString("id")
                        val news_title = jo.getString("webTitle")
                        val news_image = jo.getString("webImage")
                        val news_desc = jo.getString("webDesc")
                        val news_link = jo.getString("webUrl")

                        val news_item = News(news_id, news_title, news_image, news_desc, news_link)
                        //Toast.makeText(context, "Response: $news_item", Toast.LENGTH_LONG).show()
                        news_list.add(news_item)
                    }

                    rvfavorites.apply {
                        layoutManager = LinearLayoutManager( context)
                        adapter = favoritesAdapter(context, news_list)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 9000000
            }

            override fun getCurrentRetryCount(): Int {
                return 9000000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        //add string request to request queue
        requestQueue.add(stringRequest)
    }



}