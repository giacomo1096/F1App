package com.example.f1app.ui.history

import android.os.Bundle
import android.util.TypedValue
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
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.f1app.*


import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_circuit_details.*
import com.android.volley.RetryPolicy





class circuitsInfo(circuitId:String, circuitName:String) : Fragment() {

    private var cId = circuitId
    private var cN = circuitName
    private var cName = cN.split(",")[0]

    private val url = URL_PYTHONANYWHERE + "circuit?name="+cId

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

    private fun wiki_api_En(name :String, itemView: View){
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        val urlEn = "https://en.wikipedia.org/w/api.php?action=query&titles="+name+"&prop=pageimages&format=json&pithumbsize=100"
        val stringRequestEn = StringRequest(Request.Method.GET, urlEn,
            { response: String? ->
                try {
                    val jsonObj = JSONObject(response).toString()
                    Toast.makeText(context, "RESP Size:   " + jsonObj.split("source").size , Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "RESP Size:   " + jsonObj.split("source").size , Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen
                }
            }) { error -> error.printStackTrace() }

        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 90000
            }

            override fun getCurrentRetryCount(): Int {
                return 90000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        requestQueue.add(jsonObjectRequest)
    }

}

