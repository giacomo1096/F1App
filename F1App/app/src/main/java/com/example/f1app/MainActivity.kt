package com.example.f1app

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.f1app.ui.favAndShake.favoritesAdapter
import com.example.f1app.ui.home.News
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import kotlinx.android.synthetic.main.activity_main.*

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_favandshake.*
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    //private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1
    private val TAG = "GOOGLEAUTH"
    private lateinit var auth: FirebaseAuth

    //lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        // Configure Google Sign In
        val str = getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(str)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Getting the Button Click
        val signInbtn = findViewById<Button>(R.id.google_signIn)
        signInbtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //dialog.show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                userToken = account.idToken
                userMail = account.email
                userName = account.displayName
                val accId = checkAlreadyRegister(account.id)
                if (accId != ""){
                    userId = accId
                }
                else{
                    userId = account.id
                }
                Log.d(TAG, "firebaseAuthWithGoogle:" + userId)

                firebaseAuthWithGoogle(userToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                //dialog.dismiss()
            }
        }
    }

    private fun checkAlreadyRegister(ID : String): String {
        val url = URL_PYTHONANYWHERE + "checkemail?email="+userMail

        val requestQueue = Volley.newRequestQueue(this)
        var accountId = ""
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if( response.getString("userId") != "none"){
                        accountId = response.getString("userId")
                        Toast.makeText(this, "Welcome $userName!", Toast.LENGTH_SHORT).show()

                    }
                    else{
                        createNewAccount(ID)
                        Toast.makeText(this, "Welcome $userName!", Toast.LENGTH_SHORT).show()
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
        return accountId
    }

    private fun createNewAccount(ID: String) {
        val url = URL_PYTHONANYWHERE + "signin"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonobj = JSONObject()
        jsonobj.put("userid", ID)
        jsonobj.put("email", userMail)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonobj,
            { response ->
                try {
                    //Toast.makeText(this, "new Account", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    //e.printStackTrace()
                }
            }
        )  //Method that handles error in volley
        { error: VolleyError ->
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "ErrorRRRRR", Toast.LENGTH_SHORT).show()
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)  //decomm
                    val home = Intent(this,HomepageActivity::class.java)
                    startActivity(home) // takes the user to the signup activity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null) //decomm
                    //dialog.dismiss()
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            //Toast.makeText(this@MainActivity, "User : "+ userName+" "+ userMail, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //Toast.makeText(this@MainActivity, "$currentUser" , Toast.LENGTH_SHORT).show()
        updateUI(currentUser)
    }


}