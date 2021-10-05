package com.example.f1app.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.SharedViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*

import com.example.f1app.R


class historyFragment : Fragment(), circuitAdapter.ItemClickListener {
    var adapter: circuitAdapter? = null
    private lateinit var viewModel : SharedViewModel
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val url = "http://192.168.1.139:8000/circuits"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendAndRequestResponse()

        // data to populate the RecyclerView with
        val animalNames: ArrayList<String> = ArrayList()
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")

        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvcircuits)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = circuitAdapter(context, animalNames)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter
    }

    private fun sendAndRequestResponse() {
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context)
        //String Request initialized
        mStringRequest = StringRequest(Request.Method.GET, url, object :
            Response.Listener<String?> {
            override  fun onResponse(response: String?) {
                Toast.makeText(context,  "Response :$response", Toast.LENGTH_LONG).show()
                //Toast.makeText(applicationContext, "Response :$response", Toast.LENGTH_LONG)
                // .show() //display the response on screen
            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.i(TAG, "Error :" + error.toString())
            }
        })
        mRequestQueue!!.add(mStringRequest)
    }
    override fun onItemClick(view: View?, position: Int) {
        Toast.makeText(
            context,
            "You clicked " + adapter!!.getItem(position) + " on row number " + position,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private val TAG = historyFragment::class.java.name
    }
}

