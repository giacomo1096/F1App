package com.example.f1app

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var accountName: String = ""
    var googleIdToken: String = ""
    var googleEmail: String = ""

}