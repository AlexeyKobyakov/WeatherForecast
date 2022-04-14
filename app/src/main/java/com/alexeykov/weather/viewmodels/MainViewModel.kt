package com.alexeykov.weather.viewmodels

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alexeykov.weather.R

class MainViewModel : ViewModel() {

    lateinit var navController: NavController

    fun navigateToDetails() {
        val bundle = Bundle().apply { putString("city", "Москва") }
        navController.navigate(R.id.action_MainFragment_to_DetailsFragment, bundle)
    }

    fun navigateToAddCity() {
        navController.navigate(R.id.action_MainFragment_to_AddCityFragment)
    }
}