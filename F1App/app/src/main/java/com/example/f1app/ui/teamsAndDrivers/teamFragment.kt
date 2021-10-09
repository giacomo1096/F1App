package com.example.f1app.ui.teamsAndDrivers

import android.annotation.SuppressLint
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.fragment_team.*
import org.json.JSONException

class teamFragment(teamId:String, teamName:String) : Fragment() {
    private val url_driver = "http://192.168.1.139:8000/team?name="+teamId
    val teamName = teamName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url_driver, null,
            { response ->
                try {
                    val nationality = response.getString("nationality")
                    val totChampWin = response.getString("totalChampRace")
                    val totWinsRace = response.getString("totalWinsRace")
                    itemView.findViewById<TextView>(R.id.teamName).text = teamName
                    itemView.findViewById<TextView>(R.id.nationality).text = nationality
                    itemView.findViewById<TextView>(R.id.totChampWin).text = totChampWin
                    itemView.findViewById<TextView>(R.id.totWinRace).text = totWinsRace

                    val currentStandInfo = response.getJSONObject("standInfo")


                    val driversGrid = itemView.findViewById<GridLayout>(R.id.currDriversGrid)
                    val currentGrid = itemView.findViewById<GridLayout>(R.id.currentGrid)

                    if(currentStandInfo.getString("rank") == "0"){
                        currentGrid.removeAllViews()
                        val txt : TextView = TextView(context)
                        txt.text = "Not in current championship"
                        val row = GridLayout.spec(0, 1)
                        val colspan = GridLayout.spec(0, 1)
                        val gridLayoutParam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, colspan)
                        currentGrid.addView(txt, gridLayoutParam)
                        currentGrid.setVisibility(View.VISIBLE)

                        driversGrid.removeAllViews()
                        val txt1 : TextView = TextView(context)
                        txt1.text = "No Current Drivers"
                        val gridLayoutParam1: GridLayout.LayoutParams = GridLayout.LayoutParams(row, colspan)
                        driversGrid.addView(txt1, gridLayoutParam1)
                        driversGrid.setVisibility(View.VISIBLE)
                    }
                    else{
                        itemView.findViewById<TextView>(R.id.position).text = currentStandInfo.getString("rank")
                        itemView.findViewById<TextView>(R.id.points).text = currentStandInfo.getString("points")
                        itemView.findViewById<TextView>(R.id.wins).text = currentStandInfo.getString("wins")

                        val drivers = response.getJSONArray("currentDrivers")
                        driversGrid.removeAllViews()

                        for (i in 0 until drivers.length()) {
                            val driverName : TextView = TextView(context)
                            val driverNumber: TextView = TextView(context)
                            val jsonObject = drivers.getJSONObject(i)
                            driverNumber.text = " "+ jsonObject.getString("permanentNumber")
                            driverNumber.setOnClickListener {
                                val fragment: Fragment = driverFragment(jsonObject.getString("driverId"))
                                val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            val row = GridLayout.spec(i, 1)
                            val col1 =  GridLayout.spec(0, 1)
                            val gridLayoutParamTeam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col1)
                            driversGrid.addView(driverNumber, gridLayoutParamTeam)
                            driversGrid.setVisibility(View.VISIBLE)

                            driverName.text = "  "+ jsonObject.getString("driverSurname") + "  " + jsonObject.getString("driverName")
                            driverName.setOnClickListener {
                                val fragment: Fragment = driverFragment(jsonObject.getString("driverId"))
                                val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            val col2 =  GridLayout.spec(1, 1)
                            val gridLayoutParamNation: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col2)
                            driversGrid.addView(driverName, gridLayoutParamNation)
                        }
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