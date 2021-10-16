package com.example.f1app.ui.favAndShake

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.ui.home.News
import com.example.f1app.R
import com.example.f1app.URL_PYTHONANYWHERE
import com.example.f1app.userId
import com.example.f1app.userPrefeNewsId
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class favoritesAdapter(contextFrag: Context, jsonResponses:MutableList<News>) : RecyclerView.Adapter<favoritesAdapter.ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<News> = jsonResponses

    private val urlDelete = URL_PYTHONANYWHERE + "deletefavorite"
    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val news_title: TextView
        val news_desc: TextView
        val news_link: TextView
        val news_like: TextView
        val news_pref: ImageView
        val news_no_pref: ImageView
        val ivBasicImage: ImageView

        init {
            news_title = itemView.findViewById(R.id.news_title)
            news_desc = itemView.findViewById(R.id.news_desc)
            news_link = itemView.findViewById(R.id.news_link)
            news_no_pref = itemView.findViewById(R.id.empty_star)
            news_like = itemView.findViewById(R.id.likes)
            news_pref = itemView.findViewById(R.id.star)
            news_pref.visibility = View.VISIBLE
            news_no_pref.visibility = View.GONE
            ivBasicImage = itemView.findViewById<View>(R.id.news_img) as ImageView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.news_title.text = resp.get(i).title.substring(1)
        val imageUri = resp.get(i).image
        Picasso.get().load(imageUri).into(viewHolder.ivBasicImage)
        viewHolder.news_desc.text = resp.get(i).desc
        viewHolder.news_link.text = resp.get(i).link
        viewHolder.news_link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resp.get(i).link))
            context.startActivity(browserIntent)
        }

        viewHolder.news_pref.visibility = View.VISIBLE
        viewHolder.news_no_pref.visibility = View.GONE
        getPrefe(viewHolder, i)

        viewHolder.news_pref.setOnClickListener {
            viewHolder.news_pref.visibility = View.GONE
            viewHolder.news_no_pref.visibility = View.VISIBLE
            viewHolder.news_like.visibility = View.GONE
            deletePrefe(i)
        }

    }

    override fun getItemCount(): Int {
        return resp.size
    }

    private fun deletePrefe(i : Int){
        val requestQueue = Volley.newRequestQueue(context)
        val jsonobj = JSONObject()
        jsonobj.put("newsId", resp.get(i).id)
        jsonobj.put("userId", userId)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, urlDelete, jsonobj,
            { response ->
                try {
                    //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    val fragment: Fragment = favAndShakeFragment()
                    val fragmentManager: FragmentManager =
                        (context as AppCompatActivity).getSupportFragmentManager()
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }catch (e:Exception){
                    Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                }
            }) { error ->  Toast.makeText(context, "Volley error", Toast.LENGTH_LONG).show()
        }

        requestQueue.add(jsonObjectRequest)
        userPrefeNewsId.remove(resp.get(i).id)
    }

    @SuppressLint("SetTextI18n")
    private fun getPrefe(viewHolder: ViewHolder, i: Int) {
        val url = URL_PYTHONANYWHERE + "favoritesNumber?newsId="+resp.get(i).id
        val requestQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET,url, null,
            { response ->
                // Process the json
                try {
                    //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    viewHolder.news_like.visibility = View.VISIBLE
                    var numb = response.getInt("count")
                    if (numb != 0){
                        viewHolder.news_like.text = "Other " + numb + " users save this article"
                    }
                }catch (e:Exception){
                    Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                }

            }, {
                // Error in request
                //Toast.makeText(context, "Volley error: $it", Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

}
