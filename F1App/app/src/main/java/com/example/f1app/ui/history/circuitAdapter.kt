package com.example.f1app.ui.history

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import com.google.android.material.internal.ContextUtils.getActivity
import kotlin.reflect.typeOf
import org.json.JSONException

import org.json.JSONObject

import org.json.JSONArray

import com.android.volley.toolbox.JsonObjectRequest


class circuitAdapter(contextFrag: Context, jsonResponses:MutableList<Map<String,String>>) : RecyclerView.Adapter<circuitAdapter.ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<Map<String,String>> = jsonResponses

    //private var mRequestQueue: RequestQueue? = null
    //private var mStringRequest: StringRequest? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circuitName: TextView
        var circuitItem: LinearLayout
        var cont: View = itemView

        init {
            circuitName = itemView.findViewById(R.id.circuitName)
            circuitItem = itemView.findViewById(R.id.item)
            //Toast.makeText(contexf, "DEVI FUNGERE :$resp", Toast.LENGTH_LONG).show() //display the response on screen

            circuitItem.setOnClickListener {
                var position: Int = getAdapterPosition()
                val context = itemView.context
                val id = resp.get(position).keys.toString()

                Toast.makeText(context, "ID :$id", Toast.LENGTH_LONG).show() //display the response on screen

                /*
                val intent = Intent(context, circuitDetails::class.java).apply {
                    putExtra("NUMBER", position)
                    putExtra("ID", id)
                }
                context.startActivity(intent)*/
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.circuit_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.circuitName.text = resp.get(i).values.toString()
    }

    override fun getItemCount(): Int {
        return resp.size //kode è content della riga quindi va preso dalla risposta
    }

}
