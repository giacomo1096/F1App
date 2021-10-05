package com.example.f1app.ui.lastRace

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.f1app.SharedViewModel


// https://guides.codepath.com/android/using-the-recyclerview
// https://www.geeksforgeeks.org/how-to-implement-swipe-down-to-refresh-in-android-using-android-studio/

// https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
// https://betterprogramming.pub/everything-to-understand-about-viewmodel-400e8e637a58

class lastRaceFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
        } ?: throw Exception("Activity is null")
    }

}