package com.alexeykov.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexeykov.weather.Repositories
import com.alexeykov.weather.databinding.FragmentAddCityBinding
import com.alexeykov.weather.viewmodels.AddCityViewModel
import com.alexeykov.weather.viewmodels.viewModelCreator

class AddCityFragment : Fragment() {
    private var _binding: FragmentAddCityBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentAddCityBinding is null")

    private val addCityViewModel: AddCityViewModel by viewModelCreator {
        AddCityViewModel(
            Repositories.localRepository,
            Repositories.cloudRepository,
            findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAddCityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddCity.setOnClickListener {
            addCityViewModel.addCity(binding.enterCityText.text.toString())
        }

        addCityViewModel.errors.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}