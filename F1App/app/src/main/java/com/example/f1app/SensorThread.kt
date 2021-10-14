package com.example.f1app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater

import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

import android.view.View
import android.view.ViewGroup

import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.f1app.ui.teamsAndDrivers.driverFragment

import java.lang.Exception
import android.content.Context.LAYOUT_INFLATER_SERVICE
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import android.widget.PopupWindow
import android.view.MotionEvent

import android.view.Gravity
import android.view.View.OnTouchListener


class SensorThread(val cont:Context){

    private var mShaker: Sensor? = null
    private val url = URL_PYTHONANYWHERE+ "driverStandings"

    init{
        val thread = Thread{
            try{
                mShaker = Sensor(this, cont)
                mShaker!!.setOnShakeListener(object : Sensor.OnShakeListener {
                    override fun onShake() {
                        Toast.makeText(cont, "SI SHAKERAAAAAAA", Toast.LENGTH_LONG).show() //display the response on screen
                        prepareData(View.inflate(cont, R.layout.standing_result_shake, null))
                    }
                })

            } catch (e:Exception){ e.printStackTrace() }
        }

        thread.start()
    }

    private fun prepareData(itemView: View) {
        val popupWindow = PopupWindow(itemView, 900,ViewGroup.LayoutParams.WRAP_CONTENT , true)
        val requestQueue = Volley.newRequestQueue(cont)
        //Create new String request

        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val driver_list: MutableList<JSONObject> = mutableListOf<JSONObject>()
                    val resultArray = response.getJSONArray("list")
                    for (i in 0 until resultArray.length()) {
                        val jo = resultArray.getJSONObject(i)
                        driver_list.add(jo)
                    }

                    val sens = itemView.findViewById<LinearLayout>(R.id.sensorThread)
                    sens.visibility= View.VISIBLE
                    val standGrid = itemView.findViewById<GridLayout>(R.id.standingsGrid)
                    standGrid.removeAllViews()

                    for (i in 0 until driver_list.size) {
                        val jsonObject = driver_list.get(i)

                        val position: TextView = TextView(cont)
                        position.text = jsonObject.getString("driverPosition")
                        val row = GridLayout.spec(i, 1)
                        val col =  GridLayout.spec(0, 1)
                        val gridLayoutParamPosition: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col)
                        standGrid.addView(position, gridLayoutParamPosition)

                        val name : TextView = TextView(cont)
                        val driverId = jsonObject.getString("driverId")
                        name.text =jsonObject.getString("driverName") +" "+jsonObject.getString("driverSurname")
                        name.setOnClickListener {
                            val fragment: Fragment = driverFragment(driverId)
                            val fragmentManager: FragmentManager = ( cont as AppCompatActivity).getSupportFragmentManager()
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        val col1 =  GridLayout.spec(1, 1)
                        val gridLayoutParamName: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col1)
                        standGrid.addView(name, gridLayoutParamName)


                        val team: TextView = TextView(cont)
                        team.text = jsonObject.getString("driverTeam")
                        val col2 =  GridLayout.spec(2, 1)
                        val gridLayoutParamTeam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col2)
                        standGrid.addView(team, gridLayoutParamTeam)

                        val points: TextView = TextView(cont)
                        points.text = jsonObject.getString("driverPoints")
                        val col3 =  GridLayout.spec(3, 1)
                        val gridLayoutParamPoints: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col3)
                        standGrid.addView(points, gridLayoutParamPoints)

                    }

                    popupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0)

                    itemView.setOnTouchListener(OnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    })

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(cont, error.message, Toast.LENGTH_SHORT).show()
        }

        //add string request to request queue
        requestQueue.add(stringRequest)
    }


}
