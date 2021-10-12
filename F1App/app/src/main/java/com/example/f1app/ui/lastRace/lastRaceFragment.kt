package com.example.f1app.ui.lastRace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import com.example.f1app.newsAdapter
import kotlinx.android.synthetic.main.activity_homepage.*
import org.json.JSONArray
import org.json.JSONException
import com.squareup.picasso.Picasso

class lastRaceFragment : Fragment() {

    private val url_nextRace = "http://192.168.1.51:8000/NextRace"
    private val url_lastRace = "http://192.168.1.51:8000/lastRace"

    private fun next_race_api(itemView: View) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url_nextRace, null,
            { response ->
                try {

                    val raceInfo = response.getJSONObject("raceInfo")
                    var coord = raceInfo.getString("raceLocation")
                    var temp = coord.split(",")
                    var lat = temp[0]
                    var long = temp[1]
                    weather_api(itemView, lat, long)
                    //itemView.findViewById<TextView>(R.id.lat_coord).text = lat
                    //itemView.findViewById<TextView>(R.id.long_coord).text = long
                    //Toast.makeText(context, lat, Toast.LENGTH_LONG).show() //display the response on screen

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

    }

    private fun weather_api(itemView: View, lat: String, long: String) {
        //Toast.makeText(context, lat + " " + long, Toast.LENGTH_LONG).show() //display the response on screen

        val url_weather = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+long+"&units=metric&appid=031ea825c61cc701c0117d918a0ab5a9"
        //Toast.makeText(context, url_weather, Toast.LENGTH_LONG).show() //display the response on screen
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest_weather = JsonObjectRequest(
            Request.Method.GET, url_weather, null,
            { response ->
                try {

                    val jsonArray = response.getJSONArray("weather")
                    val weatherInfo = jsonArray.getJSONObject(0) // weatherInfo
                    itemView.findViewById<TextView>(R.id.weather).text = weatherInfo.getString("main")
                    itemView.findViewById<TextView>(R.id.weather_description).text = weatherInfo.getString("description")
                    val icon = weatherInfo.getString("icon")
                    val url_icon = "http://openweathermap.org/img/w/"+icon+".png"
                    //Toast.makeText(context, url_icon, Toast.LENGTH_LONG).show() //display the response on screen
                    Picasso.get().load(url_icon).into(itemView.findViewById<ImageView>(R.id.weather_img))

                    val tempInfo = response.getJSONObject("main")
                    itemView.findViewById<TextView>(R.id.weather_temperature).text = tempInfo.getString("temp")

                    val visibilityInfo = response.getInt("visibility")
                    itemView.findViewById<TextView>(R.id.weather_visibility).text = visibilityInfo.toString()

                    val windInfo = response.getJSONObject("wind")
                    itemView.findViewById<TextView>(R.id.weather_wind).text = windInfo.getString("speed")



                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest_weather)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //sendAndRequestResponse(jsonResponses)
        return inflater.inflate(R.layout.fragment_lastrace, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        super.onViewCreated(itemView, savedInstanceState)

        next_race_api(itemView)

        //Last Race
        val requestQueue = Volley.newRequestQueue(context)
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


