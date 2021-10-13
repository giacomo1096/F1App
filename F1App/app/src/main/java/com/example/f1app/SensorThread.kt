package com.example.f1app


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.f1app.ui.teamsAndDrivers.driverFragment
import com.example.f1app.ui.teamsAndDrivers.teamFragment
import java.lang.Exception


@SuppressLint("WrongConstant")
class SensorThread @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ) : LinearLayout(context, attrs, defStyleAttr){

    private var mShaker: Sensor? = null
    private val url = URL_PYTHONANYWHERE+ "driverStandings"
    init{
        val thread = Thread{
            try{
                mShaker = Sensor(this, context)
                mShaker!!.setOnShakeListener(object : Sensor.OnShakeListener {
                    override fun onShake() {
                        Toast.makeText(context, "SI SHAKERAAAAAAA", Toast.LENGTH_LONG).show() //display the response on screen
                        prepareData()
                    }
                })

            } catch (e:Exception){ e.printStackTrace() }
        }

        thread.start()
    }

    @SuppressLint("SetTextI18n")
    private fun prepareData() {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
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

                    val sens = findViewById<SensorThread>(R.id.sensorThread)
                    sens.visibility= View.VISIBLE
                    val standGrid = findViewById<GridLayout>(R.id.standingsGrid)
                    standGrid.removeAllViews()

                    for (i in 0 until driver_list.size) {
                        val jsonObject = driver_list.get(i)

                        val position: TextView = TextView(context)
                        position.text = jsonObject.getString("driverPosition")
                        val row = GridLayout.spec(i, 1)
                        val col =  GridLayout.spec(0, 1)
                        val gridLayoutParamPosition: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col)
                        standGrid.addView(position, gridLayoutParamPosition)

                        val name : TextView = TextView(context)
                        val driverId = jsonObject.getString("driverId")
                        name.text =jsonObject.getString("driverName") +" "+jsonObject.getString("driverSurname")
                        name.setOnClickListener {
                            val fragment: Fragment = driverFragment(driverId)
                            val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        val col1 =  GridLayout.spec(1, 1)
                        val gridLayoutParamName: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col1)
                        standGrid.addView(name, gridLayoutParamName)


                        val team: TextView = TextView(context)
                        team.text = jsonObject.getString("driverTeam")
                        val col2 =  GridLayout.spec(2, 1)
                        val gridLayoutParamTeam: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col2)
                        standGrid.addView(team, gridLayoutParamTeam)

                        val points: TextView = TextView(context)
                        points.text = jsonObject.getString("driverPoints")
                        val col3 =  GridLayout.spec(3, 1)
                        val gridLayoutParamPoints: GridLayout.LayoutParams = GridLayout.LayoutParams(row, col3)
                        standGrid.addView(points, gridLayoutParamPoints)

                    }

                    val close = findViewById<TextView>(R.id.close)
                    close.setOnClickListener {
                        sens.visibility= View.GONE
                        standGrid.removeAllViews()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }

        //add string request to request queue
        requestQueue.add(stringRequest)
    }

}
