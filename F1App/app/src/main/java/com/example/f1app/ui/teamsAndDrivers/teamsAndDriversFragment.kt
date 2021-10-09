package com.example.f1app.ui.teamsAndDrivers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import com.example.f1app.SharedViewModel
import org.json.JSONException
import com.android.volley.DefaultRetryPolicy
import com.example.f1app.HomepageActivity
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.fragment_teamsdrivers.*


// https://guides.codepath.com/android/using-the-recyclerview
// https://www.geeksforgeeks.org/how-to-implement-swipe-down-to-refresh-in-android-using-android-studio/

// https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
// https://betterprogramming.pub/everything-to-understand-about-viewmodel-400e8e637a58

class teamsAndDriversFragment : Fragment() {

    private val url_drivers = "http://192.168.1.225:8000/drivers"
    private val url_teams = "http://192.168.1.225:8000/teams"
    var query = 0 //0 drivers, 1 teams
    val jsonResponses: MutableList<Map<String,String>> = mutableListOf<Map<String,String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teamsdrivers, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url_drivers, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("list")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val driver_id = jsonObject.getString("driverId")
                        val driver_name = jsonObject.getString("driverName")
                        val driver_surname = jsonObject.getString("driverSurname")
                        val driver = driver_name+' '+driver_surname
                        val item = mapOf(driver_id to driver)
                        jsonResponses.add(item)
                    }
                    //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                    teams_drivers.apply {
                        // set a LinearLayoutManager to handle Android
                        // RecyclerView behavior
                        layoutManager = LinearLayoutManager(activity)
                        // set the custom adapter to the RecyclerView
                        adapter = teamsDriversAdapter(context, jsonResponses, query)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG)
                        .show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        requestQueue.add(jsonObjectRequest)

        val drivers_button = itemView.findViewById<Button>(R.id.buttonDrivers)
        drivers_button.setOnClickListener{
            jsonResponses.clear()
            query = 0
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url_drivers, null,
                { response ->
                    try {
                        val jsonArray = response.getJSONArray("list")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val driver_id = jsonObject.getString("driverId")
                            val driver_name = jsonObject.getString("driverName")
                            val driver_surname = jsonObject.getString("driverSurname")
                            val driver = driver_name+' '+driver_surname
                            val item = mapOf(driver_id to driver)
                            jsonResponses.add(item)
                        }
                        //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                        teams_drivers.apply {
                            // set a LinearLayoutManager to handle Android
                            // RecyclerView behavior
                            layoutManager = LinearLayoutManager(activity)
                            // set the custom adapter to the RecyclerView
                            adapter = teamsDriversAdapter(context, jsonResponses, query)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG)
                            .show() //display the response on screen

                    }
                }) { error -> error.printStackTrace() }

            jsonObjectRequest.setRetryPolicy(
                DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )

            requestQueue.add(jsonObjectRequest)

        }

        val teams_button = itemView.findViewById<Button>(R.id.buttonTeams)
        teams_button.setOnClickListener{
            jsonResponses.clear()
            query = 1
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url_teams, null,
                { response ->
                    try {
                        val jsonArray = response.getJSONArray("list")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val team_id = jsonObject.getString("teamId")
                            val team_name = jsonObject.getString("teamName")
                            val item = mapOf(team_id to team_name)
                            jsonResponses.add(item)
                        }
                        //Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                        teams_drivers.apply {
                            // set a LinearLayoutManager to handle Android
                            // RecyclerView behavior
                            layoutManager = LinearLayoutManager(activity)
                            // set the custom adapter to the RecyclerView
                            adapter = teamsDriversAdapter(context, jsonResponses, query)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG)
                            .show() //display the response on screen

                    }
                }) { error -> error.printStackTrace() }

            jsonObjectRequest.setRetryPolicy(
                DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )

            requestQueue.add(jsonObjectRequest)

        }

    }


}