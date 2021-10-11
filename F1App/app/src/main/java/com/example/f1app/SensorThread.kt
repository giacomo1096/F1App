package com.example.f1app


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.ui.history.circuitAdapter
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.activity_sensor.*
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.lang.Exception


class SensorThread @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr){

    //var context : Context = context
    private var mShaker: Sensor? = null

    init{
        val thread = Thread{
            try{

                mShaker = Sensor(this, context)
                mShaker!!.setOnShakeListener(object : Sensor.OnShakeListener {
                    override fun onShake() {
                        Toast.makeText(context, "SI SHAKERAAAAAAA", Toast.LENGTH_LONG).show() //display the response on screen

                    }
                })

            } catch (e:Exception){ e.printStackTrace() }
        }

        thread.start()

        }

}
