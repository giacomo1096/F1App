package com.example.f1app.ui.history

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.f1app.*

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.android.volley.RetryPolicy


class circuitsInfo(circuitId:String, circuitName:String) : Fragment() {

    private var cId = circuitId
    private var cN = circuitName
    private var cName = cN.split(",")[0]

    private val url = URL_PYTHONANYWHERE + "circuit?name="+cId

    fun estractData(grid : GridLayout, jsonArray: JSONArray){

        var pole_race_bol = 0 //0 pole, 1 race

        val number: TextView = TextView(context)
        number.text = "Number"
        number.setTextColor(Color.BLACK)
        number.setTextSize(16F)
        number.setTypeface(Typeface.DEFAULT_BOLD);
        val row_number = GridLayout.spec(0, 1)
        val col_number =  GridLayout.spec(0, 1)
        val gridLayoutParamTeam_number: GridLayout.LayoutParams = GridLayout.LayoutParams(row_number, col_number)
        grid.addView(number, gridLayoutParamTeam_number)
        grid.setVisibility(View.VISIBLE)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val numb: TextView = TextView(context)

            numb.text = jsonObject.getString("number")
            numb.setTextColor(Color.BLACK)
            numb.setTextSize(16F)

            val row = GridLayout.spec(i+1, 1)
            val col1 = GridLayout.spec(0, 1)
            val gridLayoutParam: GridLayout.LayoutParams =
                GridLayout.LayoutParams(row, col1)
            grid.addView(numb, gridLayoutParam)
            grid.setVisibility(View.VISIBLE)

            //set the boolean variable
            if (jsonObject.length() < 5){ pole_race_bol = 1 }
        }

        val n: TextView = TextView(context)
        n.text = "    Name"
        n.setTextColor(Color.BLACK)
        n.setTextSize(16F)
        n.setTypeface(Typeface.DEFAULT_BOLD);
        val row_n = GridLayout.spec(0, 1)
        val col_n =  GridLayout.spec(1, 1)
        val gridLayoutParam_n: GridLayout.LayoutParams = GridLayout.LayoutParams(row_n, col_n)
        grid.addView(n, gridLayoutParam_n)
        grid.setVisibility(View.VISIBLE)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name: TextView = TextView(context)

            name.text = "    "+jsonObject.getString("name") +" "+ jsonObject.getString("surname")
            name.setTextColor(Color.BLACK)
            name.setTextSize(16F)

            val row = GridLayout.spec(i+1, 1)
            val col1 = GridLayout.spec(1, 1)
            val gridLayoutParam: GridLayout.LayoutParams =
                GridLayout.LayoutParams(row, col1)
            grid.addView(name, gridLayoutParam)
            grid.setVisibility(View.VISIBLE)
        }

        val t: TextView = TextView(context)
        t.text = "    Team"
        t.setTextColor(Color.BLACK)
        t.setTextSize(16F)
        t.setTypeface(Typeface.DEFAULT_BOLD);
        val row_t = GridLayout.spec(0, 1)
        val col_t =  GridLayout.spec(2, 1)
        val gridLayoutParam_t: GridLayout.LayoutParams = GridLayout.LayoutParams(row_t, col_t)
        grid.addView(t, gridLayoutParam_t)
        grid.setVisibility(View.VISIBLE)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val team: TextView = TextView(context)

            team.text = "    "+jsonObject.getString("team")
            team.setTextColor(Color.BLACK)
            team.setTextSize(16F)

            val row = GridLayout.spec(i+1, 1)
            val col1 = GridLayout.spec(2, 1)
            val gridLayoutParam: GridLayout.LayoutParams =
                GridLayout.LayoutParams(row, col1)
            grid.addView(team, gridLayoutParam)
            grid.setVisibility(View.VISIBLE)
        }


        if (pole_race_bol == 0) { //pole result -> add time

            val tempo: TextView = TextView(context)
            tempo.text = "    Time"
            tempo.setTextColor(Color.BLACK)
            tempo.setTextSize(16F)
            tempo.setTypeface(Typeface.DEFAULT_BOLD);
            val row_tempo = GridLayout.spec(0, 1)
            val col_tempo = GridLayout.spec(3, 1)
            val gridLayoutParam_tempo: GridLayout.LayoutParams =
                GridLayout.LayoutParams(row_tempo, col_tempo)
            grid.addView(tempo, gridLayoutParam_tempo)
            grid.setVisibility(View.VISIBLE)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val time: TextView = TextView(context)

                time.text = "    " + jsonObject.getString("time")
                time.setTextColor(Color.BLACK)
                time.setTextSize(16F)

                val row = GridLayout.spec(i + 1, 1)
                val col1 = GridLayout.spec(3, 1)
                val gridLayoutParam: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(row, col1)
                grid.addView(time, gridLayoutParam)
                grid.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun wiki_api_En(name :String, itemView: View){
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        val urlEn = "https://en.wikipedia.org/w/api.php?action=query&titles="+name+"&prop=pageimages&format=json&pithumbsize=100"
        val stringRequestEn = StringRequest(Request.Method.GET, urlEn,
            { response: String? ->
                try {
                    val jsonObj = JSONObject(response).toString()
                    //Toast.makeText(context, "RESP Size:   " + jsonObj.split("source").size , Toast.LENGTH_SHORT).show()
                    if (jsonObj.split("source").size > 1) {
                        val src: String = jsonObj.split("source")[1].split("\"")[2]
                        Picasso.get().load(src)
                            .into(itemView.findViewById<ImageView>(R.id.circuit_img))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(stringRequestEn)
    }
    private fun wiki_api_It(name :String, itemView: View){
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        val urlIt = "https://it.wikipedia.org/w/api.php?action=query&titles="+name+"&prop=pageimages&format=json&pithumbsize=100"
        val stringRequestIt = StringRequest(Request.Method.GET, urlIt,
            { response: String? ->
                try {
                    val jsonObj = JSONObject(response).toString()
                    //Toast.makeText(context, "RESP Size:   " + jsonObj.split("source").size , Toast.LENGTH_SHORT).show()
                    if (jsonObj.split("source").size > 1) {
                        val src: String = jsonObj.split("source")[1].split("\"")[2]
                        Picasso.get().load(src)
                            .into(itemView.findViewById<ImageView>(R.id.circuit_img))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(stringRequestIt)
    }

    fun createGrid(response: JSONObject, root: View){

        //pole
        val gridPole = root.findViewById(R.id.gridPole) as GridLayout
        val jsonArrayPoles = response.getJSONArray("poles")
        if(jsonArrayPoles.length() == 0){
            gridPole.removeAllViews()
            val txt: TextView = TextView(context)
            txt.text = "No information available"
            val row = GridLayout.spec(0, 1)
            val col = GridLayout.spec(0, 1)
            val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col)
            gridPole.addView(txt, gridLayoutParam)
            gridPole.setVisibility(View.VISIBLE)
        }else {
            gridPole.removeAllViews()
            estractData(gridPole, jsonArrayPoles)
        }

        //race
        val gridResults = root.findViewById(R.id.gridResults) as GridLayout
        val jsonArrayResults = response.getJSONArray("results")
        if(jsonArrayPoles.length() == 0) {
            gridResults.removeAllViews()
            val txt: TextView = TextView(context)
            txt.text = "No information available"
            val row = GridLayout.spec(0, 1)
            val col = GridLayout.spec(0, 1)
            val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col)
            gridResults.addView(txt, gridLayoutParam)
            gridResults.setVisibility(View.VISIBLE)
        }else {
            gridResults.removeAllViews()
            estractData(gridResults, jsonArrayResults)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_circuit_details, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        var name_list : List<String> = cName.split(" ")
        var nameCircuit:String = name_list[0]
        if (name_list.size > 1){
            for (c in 1 until name_list.size) {
                nameCircuit = nameCircuit + "_" + name_list[c]
            }
        }
        wiki_api_En(nameCircuit, itemView)
        wiki_api_It(nameCircuit, itemView)

        itemView.findViewById<TextView>(R.id.circuitName).text = cN

        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    createGrid(response, itemView)
                    //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                 } catch (e: JSONException) {
                    e.printStackTrace()
                    //Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen
                }
            }) { error -> error.printStackTrace() }

        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
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
        requestQueue.add(jsonObjectRequest)
    }

}

