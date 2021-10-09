package com.example.f1app.ui.teamsAndDrivers

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.f1app.R
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity

class teamsDriversAdapter(contextFrag: Context, jsonResponses:MutableList<Map<String,String>>, query:Int) : RecyclerView.Adapter<teamsDriversAdapter.ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<Map<String,String>> = jsonResponses
    private var query_bol = query

    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var driverInfo: TextView
        var driverItem: LinearLayout
        var cont: View = itemView

        init {
            driverInfo = itemView.findViewById(R.id.driverInfo)
            driverItem = itemView.findViewById(R.id.driver_item)
            //Toast.makeText(contexf, "DEVI FUNGERE :$resp", Toast.LENGTH_LONG).show() //display the response on screen

            driverInfo.setOnClickListener {

                var position: Int = getAdapterPosition()
                val context = itemView.context
                val id = resp.get(position).keys.toString()

                Toast.makeText(context, "ID :$id", Toast.LENGTH_LONG).show() //display the response on screen

                /*
                val fragment: Fragment = teamsDriversDetails(id)
                val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val key = resp.get(i).keys.toString().removeSurrounding("[","]")
        if(query_bol == 0){
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.driver_row, viewGroup, false)
            return ViewHolder(v)

        } else{
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.team_row, viewGroup, false)
            return ViewHolder(v)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.driverInfo.text = resp.get(i).values.toString().removeSurrounding("[","]")
    }

    override fun getItemCount(): Int {
        return resp.size
    }

}
