package com.alexeykov.weather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexeykov.weather.R
import com.alexeykov.weather.databinding.FragmentMainBinding
import com.alexeykov.weather.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.navController = findNavController()

        binding.buttonFirst.setOnClickListener {
            mainViewModel.navigateToDetails()
        }

        binding.fab.setOnClickListener {
            mainViewModel.navigateToAddCity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}