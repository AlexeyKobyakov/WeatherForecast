package com.alexeykov.weather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexeykov.weather.Repositories
import com.alexeykov.weather.adapters.CityListAdapter
import com.alexeykov.weather.databinding.FragmentMainBinding
import com.alexeykov.weather.viewmodels.MainViewModel
import com.alexeykov.weather.viewmodels.viewModelCreator

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    private val mainViewModel: MainViewModel by viewModelCreator {
        MainViewModel(
            Repositories.localRepository,
            Repositories.cloudRepository,
            findNavController()
        )
    }

    private lateinit var adapter: CityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        adapter = CityListAdapter(mainViewModel)

        binding.CitiesRecyclerView.layoutManager = layoutManager
        binding.CitiesRecyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            mainViewModel.navigateToAddCity()
        }

        observeCities()
    }

    private fun observeCities() {
        mainViewModel.items.observe(viewLifecycleOwner) {
            adapter.renderItems(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}