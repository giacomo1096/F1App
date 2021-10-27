package com.example.f1app

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.f1app.databinding.ActivityHomepageBinding
import android.widget.Toast
import com.android.volley.Request

import org.json.JSONException
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError

import com.android.volley.toolbox.JsonObjectRequest
import android.content.DialogInterface
import android.media.VolumeShaper
import com.android.volley.RetryPolicy
import com.google.android.material.navigation.NavigationView


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
                Toast.makeText(this, "Bye bye $userName!", Toast.LENGTH_SHORT).show()
            }
        }
        if (id == R.id.delete){
            confirmAction()
            }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        getFavoriteId()

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_homepage)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favAndShake, R.id.navigation_history, R.id.navigation_home, R.id.navigation_lastRace, R.id.navigation_teamsAndDrivers

            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Initialize the NavigationView if it is present,
        // so that clicking an item takes
        // the user to the appropriate destination.
        val sideNavView = findViewById<NavigationView>(R.id.nav_view_ham)
        sideNavView?.setupWithNavController(navController)
        sideNavView?.itemIconTintList = null

        // Initialize the BottomNavigationView if it is present,
        // so that clicking an item takes
        // the user to the appropriate destination.
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.setupWithNavController(navController)
        bottomNav?.itemIconTintList = null

        SensorThread(this)
    }

    private fun confirmAction(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Hey " + userName + "!")
        builder.setMessage("Do you want to delete your account and the relative information?")
        builder.setPositiveButton("Confirm",
            DialogInterface.OnClickListener { dialog,which ->
                deleteAccount()
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()}
            })
        builder.setNegativeButton("Cancel",
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
                    Toast.makeText(this, "Your account has been deleted successfully", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        }

        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 9000000
            }

            override fun getCurrentRetryCount(): Int {
                return 9000000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })

        //add string request to request queue
        requestQueue.add(stringRequest)
    }

    private fun getFavoriteId(){
        val url = URL_PYTHONANYWHERE + "favorites?userId="+userId
        //Create request queue
        val requestQueue = Volley.newRequestQueue(this)
        //Create new String request
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val resultArray = response.getJSONArray("favorites")
                    for (i in 0 until resultArray.length()) {
                        val jo = resultArray.getJSONObject(i)
                        val news_id = jo.getString("id")
                        userPrefeNewsId.add(news_id)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        }

        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 9000000
            }

            override fun getCurrentRetryCount(): Int {
                return 9000000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })

        //add string request to request queue
        requestQueue.add(stringRequest)
    }

}