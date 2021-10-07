package com.example.f1app.ui.history

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.R
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONException
import android.widget.GridLayout.Spec
import android.view.Gravity








// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [circuitDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class circuitDetails(circuitId:String) : Fragment() {
    private var cId = circuitId.substring(1,circuitId.length-1)
    private val url = "http://192.168.1.51:8000/circuit?name="+cId

    val jsonResponses: MutableList<String> = mutableListOf<String>()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun createGrid(resultList: MutableList<String>, root: ViewGroup) {

        val gridLayout: GridLayout =  GridLayout(context);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(resultList.size);
        var  titleText: TextView
        for ( i in 0 until resultList.size) {

            Toast.makeText(context, "RESULT LIST : ${resultList.get(i)}", Toast.LENGTH_LONG).show() //display the response on screen

            titleText = TextView (context);


            val param: GridLayout.LayoutParams = GridLayout.LayoutParams()
            param.height = GridLayout.LayoutParams.WRAP_CONTENT
            param.width = GridLayout.LayoutParams.WRAP_CONTENT
            param.rightMargin = 5
            param.topMargin = 5
            param.setGravity(Gravity.CENTER)
            param.columnSpec = GridLayout.spec(1)
            param.rowSpec = GridLayout.spec(i)

            titleText.layoutParams = param
            titleText.setText(resultList.get(i));
            gridLayout.addView(titleText, i);
        }
        root.findViewById<LinearLayout>(R.id.gridLayout).addView(gridLayout)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context, "OK CAMBIO : $cId", Toast.LENGTH_LONG).show() //display the response on screen
        val root = inflater.inflate(R.layout.fragment_circuit_details, null) as ViewGroup

        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val jsonArrayPoles = response.getJSONArray("poles")
                    val jsonArrayResults = response.getJSONArray("results")
                    for (i in 0 until jsonArrayPoles.length()) {
                        val jsonObject = jsonArrayPoles.getJSONObject(i)

                        //val id = jsonObject.getString("id")
                        //val circuit_name = jsonObject.getString("circuit")
                        //val item = mapOf(id to circuit_name)
                        jsonResponses.add(jsonObject.toString())
                    }
                    createGrid(jsonResponses, root)

                    Toast.makeText(context, "Response :$jsonResponses", Toast.LENGTH_LONG).show() //display the response on screen
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "sei nel catch", Toast.LENGTH_LONG).show() //display the response on screen

                }
            }) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circuit_details, container, false)
    }

}