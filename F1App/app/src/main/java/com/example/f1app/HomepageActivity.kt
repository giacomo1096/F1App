package com.example.f1app

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.f1app.databinding.ActivityHomepageBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request

import org.json.JSONException

import org.json.JSONObject

import org.json.JSONArray

import com.android.volley.toolbox.StringRequest

import com.android.volley.toolbox.Volley

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.fragment_history.*
import androidx.annotation.NonNull
import com.android.volley.toolbox.JsonObjectRequest

import com.google.android.gms.tasks.OnCompleteListener
import android.content.DialogInterface








class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean {
        val id = item.getItemId()
        if (id == R.id.item1){
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        if (id == R.id.delete){
            confirmAction()
            }
        return true
    }

    private fun confirmAction(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("ATTENTION!!")
        builder.setMessage("Delate Account" + userName + " and the relative information")
        builder.setPositiveButton("Confirm",
            DialogInterface.OnClickListener { dialog,which ->
                deleteAccount()
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()}
            })
        builder.setNegativeButton(android.R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun deleteAccount() {
        val url = URL_PYTHONANYWHERE + "deleteaccount?userid="+ userId
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            { response ->
                try {
                    Toast.makeText(this, "ACCOUNT DELATED", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        }

        //add string request to request queue
        requestQueue.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_activity_homepage)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favAndShake, R.id.navigation_history, R.id.navigation_home, R.id.navigation_lastRace, R.id.navigation_teamsAndDrivers

            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        SensorThread(this)
    }


}