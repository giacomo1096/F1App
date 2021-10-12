package com.example.f1app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1app.ui.teamsAndDrivers.driverFragment
import org.json.JSONObject

class sensorAdapter(contextFrag: Context, jsonResponses:MutableList<JSONObject>) : RecyclerView.Adapter<sensorAdapter.ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<JSONObject> = jsonResponses

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //val driver_id: TextView
        val driver_name: TextView
        val driver_surname: TextView
        val driver_team: TextView
        val driver_position: TextView
        val driver_points: TextView

        init {
            driver_name = itemView.findViewById(R.id.news_title)
            driver_surname = itemView.findViewById(R.id.news_desc)
            driver_team = itemView.findViewById(R.id.news_link)
            driver_position = itemView.findViewById(R.id.empty_star)
            driver_points = itemView.findViewById(R.id.star)
        }
    }

            //Toast.makeText(context, "DEVI FUNGERE :$resp", Toast.LENGTH_LONG).show() //display the response on scre

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.standing_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.driver_name.text = resp.get(i).getString("driverName")
        viewHolder.driver_surname.text =resp.get(i).getString("driverSurname")
        viewHolder.driver_team.text = resp.get(i).getString("driverTeam")
        viewHolder.driver_position.text = resp.get(i).getString("driverPosition")
        viewHolder.driver_points.text = resp.get(i).getString("driverPoints")
        viewHolder.driver_name.setOnClickListener {
            val fragment: Fragment = driverFragment(resp.get(i).getString("driverId"))
            val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    override fun getItemCount(): Int {
        return resp.size
    }

}
