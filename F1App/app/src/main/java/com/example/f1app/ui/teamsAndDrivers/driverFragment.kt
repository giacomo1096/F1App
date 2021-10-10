package com.example.f1app.ui.teamsAndDrivers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.activity_homepage.*
import org.json.JSONException
import org.json.JSONObject


import android.widget.ImageView
import com.squareup.picasso.Picasso

class driverFragment(driverId: String) : Fragment() {
    private val url_driver = "http://192.168.1.139:8000/driver?name="+driverId


    private fun wiki_api(name :String, surname : String, itemView: View) {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        val url = "https://en.wikipedia.org/w/api.php?action=query&titles="+name+"_"+surname+"&prop=pageimages&format=json&pithumbsize=100"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response: String? ->
                try {
                    val jsonObj = JSONObject(response).toString()
                    if (jsonObj.split("source").size > 1) {
                        val src: String = jsonObj.split("source")[1].split("\"")[2]
                        //Toast.makeText(context, "SOURCEEEEE:   " + src , Toast.LENGTH_SHORT).show()
                        Picasso.get().load(src)
                            .into(itemView.findViewById<ImageView>(R.id.driver_img))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }

        requestQueue.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url_driver, null,
            { response ->
                try {
                    val driverInfo = response.getJSONObject("driverInfo")
                    itemView.findViewById<TextView>(R.id.name).text = driverInfo.getString("name")
                    itemView.findViewById<TextView>(R.id.surname).text = driverInfo.getString("surname")
                    itemView.findViewById<TextView>(R.id.numb).text = driverInfo.getString("numb")
                    itemView.findViewById<TextView>(R.id.birth).text = driverInfo.getString("birth")
                    itemView.findViewById<TextView>(R.id.nation).text = driverInfo.getString("nationality")

                    wiki_api(driverInfo.getString("name"),driverInfo.getString("surname"), itemView)

                    val poleRace = response.getString("poleRaces")
                    val wonChamp = response.getString("wonChamp")
                    val wonRace = response.getString("wonRaces")
                    itemView.findViewById<TextView>(R.id.polePosition).text = poleRace
                    itemView.findViewById<TextView>(R.id.wonChamp).text = wonChamp
                    itemView.findViewById<TextView>(R.id.wonRaces).text = wonRace


                    val currentStanding = response.getJSONObject("currentStanding")
                    val grid = itemView.findViewById<GridLayout>(R.id.currentResGrid)
                    if (currentStanding.getString("position") == "0"){
                        grid.removeAllViews()
                        val txt : TextView = TextView(context)
                        txt.text = "Not in current championship"
                        val row = GridLayout.spec(0, 1)
                        val colspan = GridLayout.spec(0, 1)
                        val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, colspan)
                        grid.addView(txt, gridLayoutParam)
                        grid.setVisibility(View.VISIBLE)
                    }
                    else{
                        itemView.findViewById<TextView>(R.id.position).text = currentStanding.getString("position")
                        itemView.findViewById<TextView>(R.id.points).text = currentStanding.getString("points")
                        itemView.findViewById<TextView>(R.id.team).text = currentStanding.getString("team")
                        itemView.findViewById<TextView>(R.id.wins).text = currentStanding.getString("wins")
                    }

                    val teams = response.getJSONArray("teamsList")
                    val teamGrid = itemView.findViewById<GridLayout>(R.id.teamsGrid)
                    teamGrid.removeAllViews()

                    for (i in 0 until teams.length()) {

                        val teamName : TextView = TextView(context)
                        val teamNationality: TextView = TextView(context)
                        val jsonObject = teams.getJSONObject(i)
                        val tId = jsonObject.getString("id")

                        teamName.text = "Team: "+ jsonObject.getString("name")
                        teamName.setOnClickListener {
                            val fragment: Fragment = teamFragment(tId, jsonObject.getString("name") )
                            val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        val row = GridLayout.spec(i, 1)
                        val col1 =  GridLayout.spec(0, 1)
                        val gridLayoutParamTeam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col1)
                        teamGrid.addView(teamName, gridLayoutParamTeam)
                        teamGrid.setVisibility(View.VISIBLE)

                        teamNationality.text = "  Team Nationality: "+ jsonObject.getString("nationality")
                        teamNationality.setOnClickListener {
                            val fragment: Fragment = teamFragment(tId, jsonObject.getString("name") )
                            val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        val col2 =  GridLayout.spec(1, 1)
                        val gridLayoutParamNation: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col2)
                        teamGrid.addView(teamNationality, gridLayoutParamNation)

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