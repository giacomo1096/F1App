package com.example.f1app.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import java.security.MessageDigest
import androidx.recyclerview.widget.RecyclerView





class homeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    val news_list: MutableList<News> = mutableListOf<News>()
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        listState= savedInstanceState?.getParcelable("ListState");

        prepareData()

        mSwipeRefreshLayout = itemView.findViewById(R.id.swipe_container)

        mSwipeRefreshLayout!!.setOnRefreshListener {
                prepareData()
                mSwipeRefreshLayout!!.isRefreshing = false
        }
    }

    private fun prepareData() {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
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
                        val news_id = hashString(news_title+news_image+news_desc+news_link)

                        //Toast.makeText(this, "TEST news-title: $news_title desc: $news_desc link: $news_link", Toast.LENGTH_LONG).show() //display the response on screen

                        val news_item = News(news_id, news_title, news_image, news_desc, news_link)

                        news_list.add(news_item)
                    }

                    rvnews.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = newsAdapter(context, news_list)
                    }
                    rvnews.getLayoutManager()?.onRestoreInstanceState(listState);
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
    }

    private fun hashString( input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    override fun onRefresh() {
        prepareData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ListState", rvnews?.getLayoutManager()?.onSaveInstanceState())
    }

}