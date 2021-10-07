package com.example.f1app.ui.history

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.f1app.R
import android.annotation.SuppressLint
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity

class circuitAdapter(contextFrag: Context, jsonResponses:MutableList<Map<String,String>>) : RecyclerView.Adapter<circuitAdapter.ViewHolder>() {
    private var context : Context = contextFrag
    private var resp : MutableList<Map<String,String>> = jsonResponses

    @SuppressLint("RestrictedApi")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circuitName: TextView
        var circuitItem: LinearLayout
        var cont: View = itemView

        init {
            circuitName = itemView.findViewById(R.id.circuitName)
            circuitItem = itemView.findViewById(R.id.item)
            //Toast.makeText(contexf, "DEVI FUNGERE :$resp", Toast.LENGTH_LONG).show() //display the response on screen

            val fragment = circuitDetails()

            circuitName.setOnClickListener {
                var position: Int = getAdapterPosition()
                val context = itemView.context
                val id = resp.get(position).keys.toString()

                Toast.makeText(context, "ID :$id", Toast.LENGTH_LONG).show() //display the response on screen

                val fragment: Fragment = circuitDetails()
                val fragmentManager: FragmentManager = (context as AppCompatActivity).getSupportFragmentManager()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_homepage, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.circuit_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.circuitName.text = resp.get(i).values.toString()
    }

    override fun getItemCount(): Int {
        return resp.size
    }

}
