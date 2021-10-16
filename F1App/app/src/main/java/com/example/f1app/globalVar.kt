package com.example.f1app

import com.google.android.gms.auth.api.signin.GoogleSignInClient

const val URL_PYTHONANYWHERE= "https://formula1server.pythonanywhere.com/"
lateinit var mGoogleSignInClient: GoogleSignInClient
var userMail : String = ""
var userId : String = ""
var userName : String = ""
var userToken : String = ""
var userPrefeNewsId : MutableList<String> = mutableListOf<String>()

