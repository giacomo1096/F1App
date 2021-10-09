package com.example.f1app.ui.lastRace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import com.example.f1app.SharedViewModel
import com.example.f1app.ui.history.circuitAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_lastrace.*
import org.json.JSONException


// https://guides.codepath.com/android/using-the-recyclerview
// https://www.geeksforgeeks.org/how-to-implement-swipe-down-to-refresh-in-android-using-android-studio/

// https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
// https://betterprogramming.pub/everything-to-understand-about-viewmodel-400e8e637a58

class lastRaceFragment : Fragment() {

    private val url_nextRace = "http://192.168.1.51:8000/NextRace"
    private val url_lastRace = "http://192.168.1.51:8000/lastRace"

    val jsonResponses: MutableList<Map<String,String>> = mutableListOf<Map<String,String>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_lastrace, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        super.onViewCreated(itemView, savedInstanceState)
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url_nextRace, null,
            { response ->
                try {

                    val raceInfo = response.getJSONObject("raceInfo")
                    itemView.findViewById<TextView>(R.id.race_id).text = raceInfo.getString("race")
                    itemView.findViewById<TextView>(R.id.race_date).text = raceInfo.getString("raceDate")
                    itemView.findViewById<TextView>(R.id.race_time).text = raceInfo.getString("raceTime")
                    itemView.findViewById<TextView>(R.id.race_circuit).text = raceInfo.getString("raceCircuit")


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)

        val jsonObjectRequest_lastRace = JsonObjectRequest(
            Request.Method.GET, url_lastRace, null,
            { response ->
                try {

                    val raceInfo = response.getJSONObject("raceInfo")
                    itemView.findViewById<TextView>(R.id.last_race_id).text = raceInfo.getString("race")
                    itemView.findViewById<TextView>(R.id.last_race_date).text = raceInfo.getString("raceDate")
                    itemView.findViewById<TextView>(R.id.last_race_circuit).text = raceInfo.getString("raceCircuit")

                    val winnerInfo = response.getJSONObject("winnerInfo")
                    itemView.findViewById<TextView>(R.id.winner_number).text = winnerInfo.getString("number")
                    itemView.findViewById<TextView>(R.id.winner_name).text = winnerInfo.getString("driverName")
                    itemView.findViewById<TextView>(R.id.winner_surname).text = winnerInfo.getString("driverSurname")
                    itemView.findViewById<TextView>(R.id.winner_constructor).text = winnerInfo.getString("constructor")

                    val poleInfo = response.getJSONObject("PoleManInfo")
                    itemView.findViewById<TextView>(R.id.pole_number).text = poleInfo.getString("number")
                    itemView.findViewById<TextView>(R.id.pole_name).text = poleInfo.getString("driverName")
                    itemView.findViewById<TextView>(R.id.pole_surname).text = poleInfo.getString("driverSurname")
                    itemView.findViewById<TextView>(R.id.pole_constructor).text = poleInfo.getString("constructor")


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest_lastRace)
    }


}


