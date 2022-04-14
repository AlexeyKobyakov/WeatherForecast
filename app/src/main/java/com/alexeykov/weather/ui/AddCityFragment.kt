package com.alexeykov.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexeykov.weather.R
import com.alexeykov.weather.databinding.FragmentAddCityBinding

class AddCityFragment : Fragment() {
    private var _binding: FragmentAddCityBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentAddCityBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddCity.setOnClickListener {
            findNavController().navigate(R.id.action_AddCityFragment_pop)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}