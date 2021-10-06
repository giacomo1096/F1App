package com.example.f1app.ui.history

import android.app.PendingIntent.getActivity
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import com.google.android.material.internal.ContextUtils.getActivity
import kotlin.reflect.typeOf


class circuitAdapter(contextFrag: Context) : RecyclerView.Adapter<circuitAdapter.ViewHolder>() {
    private val kode = arrayOf("d116df5",
        "36ffc75", "f5cfe78", "5b87628",
        "db8d14e", "9913dc4", "e120f96",
        "466251b")
    private var contexf : Context = contextFrag
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val url = "http://192.168.1.139:8000/circuits"


    fun sendAndRequestResponse() {
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(contexf)
        //String Request initialized
        mStringRequest = StringRequest(Request.Method.GET, url, object :
            Response.Listener<String?> {
            override  fun onResponse(response: String?) {
                Toast.makeText(contexf, "Response :$response", Toast.LENGTH_LONG).show() //display the response on screen
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Toast.makeText(contexf, "ERROOOOOOORRRRREEEEEEEEEE", Toast.LENGTH_LONG).show() //display the response on screen
            }
        })
        mRequestQueue!!.add(mStringRequest)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circuitName: TextView
        init {
            circuitName = itemView.findViewById(R.id.circuitName)
            sendAndRequestResponse()
            /*itemView.setOnClickListener {
                var position: Int = getAdapterPosition()
                val context = itemView.context
                val intent = Intent(context, DetailPertanyaan::class.java).apply {
                    putExtra("NUMBER", position)
                    putExtra("CODE", itemKode.text)
                    putExtra("CATEGORY", itemKategori.text)
                    putExtra("CONTENT", itemIsi.text)
                }
                context.startActivity(intent)
            }*/
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.circuit_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.circuitName.text = kode[i] //kode è content della riga quindi va preso dalla risposta
    }

    override fun getItemCount(): Int {
        return kode.size //kode è content della riga quindi va preso dalla risposta
    }

}
