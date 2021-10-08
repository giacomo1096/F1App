package com.example.f1app.ui.history

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.LinearLayout




class circuitsInfo(circuitId:String) : Fragment() {

    private var cId = circuitId.substring(1,circuitId.length-1)
    private val url = "http://192.168.1.139:8000/circuit?name="+cId

    //Giorgia
    //private val url = "http://192.168.1.255:8000/circuit?name="+cId

    fun estractData(grid : GridLayout, jsonArray: JSONArray){

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name : TextView = TextView(context)
            val surname : TextView = TextView(context)
            val numb : TextView = TextView(context)
            val team : TextView = TextView(context)
            val time : TextView = TextView(context)

            name.text = jsonObject.getString("name")
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            name.setPadding(3,0,3,0)

            surname.text= jsonObject.getString("surname")
            surname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            surname.setPadding(3,0,3,0)

            numb.text = jsonObject.getString("number")
            numb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
            numb.setPadding(3,0,3,0)

            team.text = jsonObject.getString("team")
            team.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
            team.setPadding(3,0,3,0)

            val list : MutableList<TextView> = mutableListOf<TextView>()
            list.add(numb)
            list.add(name)
            list.add(surname)
            list.add(team)

            if (jsonObject.length() == 5){
                time.text = jsonObject.getString("time")
                list.add(time)
                time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
                time.setPadding(3,0,3,0)
            }


            for (j in 0 until list.size) {
                val txt : TextView = list[j]
                val row = GridLayout.spec(i, 1)
                val colspan = GridLayout.spec(j, 1)
                val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, colspan)
                grid.addView(txt, gridLayoutParam)
                grid.setVisibility(View.VISIBLE)
            }
        }

    }

    fun createGrid(response: JSONObject, root: View){

        val gridPole = root.findViewById(R.id.gridPole) as GridLayout
        gridPole.removeAllViews()

        val gridResults = root.findViewById(R.id.gridResults) as GridLayout
        gridPole.removeAllViews()

        val jsonArrayPoles = response.getJSONArray("poles")
        estractData(gridPole, jsonArrayPoles)
        val jsonArrayResults = response.getJSONArray("results")
        estractData(gridResults, jsonArrayResults)


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
                    createGrid(response, itemView)
                    //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                 } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen
                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)
    }

}

