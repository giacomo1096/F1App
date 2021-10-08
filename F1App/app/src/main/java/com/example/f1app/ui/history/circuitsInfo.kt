package com.example.f1app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONException

class circuitsInfo(circuitId:String) : Fragment() {

    private var cId = circuitId.substring(1,circuitId.length-1)
    private val url = "http://192.168.1.139:8000/circuit?name="+cId
    val jsonResponses: MutableList<String> = mutableListOf<String>()

    fun createGrid(resultList: MutableList<String>, root: View){
        val grid = root.findViewById(R.id.grid) as GridLayout
        grid.removeAllViews()

        val rowSize: Int = resultList.size //the number of bottons i have to put in GridLayout
        var columnIndex = 0 //cols index to which i add the button
        var rowIndex = 0
        for (i in 0 until rowSize) {
            val txtSrc = resultList[i]
            val txt : TextView = TextView(context)
            txt.text = txtSrc
            rowIndex++ //here i increase the row index
            columnIndex = 0

            val row = GridLayout.spec(rowIndex, 1)
            val colspan = GridLayout.spec(columnIndex, 1)
            val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, colspan)
            grid.addView(txt, gridLayoutParam)
            grid.setVisibility(View.VISIBLE)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_circuit_details, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArrayPoles = response.getJSONArray("poles")
                    val jsonArrayResults = response.getJSONArray("results")
                    for (i in 0 until jsonArrayPoles.length()) {
                        val jsonObject = jsonArrayPoles.getJSONObject(i)
                        //val id = jsonObject.getString("id")
                        //val circuit_name = jsonObject.getString("circuit")
                        //val item = mapOf(id to circuit_name)
                        jsonResponses.add(jsonObject.toString())
                    }
                    createGrid(jsonResponses, itemView)

                    Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)
    }

}

