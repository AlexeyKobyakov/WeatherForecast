package com.alexeykov.weather.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alexeykov.weather.R

class AddCityViewModel: ViewModel() {

    lateinit var navController: NavController

    fun navigateBack() {
        navController.navigate(R.id.action_AddCityFragment_pop)
    }
}