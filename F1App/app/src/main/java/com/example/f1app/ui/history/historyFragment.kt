package com.example.f1app.ui.history

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.fragment_history.*
import com.example.f1app.R
import com.example.f1app.URL_PYTHONANYWHERE
import kotlinx.android.synthetic.main.fragment_teamsdrivers.*
import org.json.JSONException
import org.json.JSONObject

class historyFragment : Fragment() {

    private val url = URL_PYTHONANYWHERE + "circuits"
    val jsonResponses: MutableList<JSONObject> = mutableListOf<JSONObject>()

    var listStatePrefe: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        listStatePrefe= savedInstanceState?.getParcelable("ListState")

        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("list")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        jsonResponses.add(jsonObject)
                    }
                    //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                    rvcircuits.apply {
                        // set a LinearLayoutManager to handle Android
                        // RecyclerView behavior
                        layoutManager = LinearLayoutManager(activity)
                        // set the custom adapter to the RecyclerView
                        adapter = circuitAdapter(context, jsonResponses)
                    }
                    rvcircuits.getLayoutManager()?.onRestoreInstanceState(listStatePrefe);
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ListState", rvcircuits?.getLayoutManager()?.onSaveInstanceState())
    }

}

