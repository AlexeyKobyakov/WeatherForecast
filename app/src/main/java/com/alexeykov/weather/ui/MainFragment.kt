package com.alexeykov.weather.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexeykov.weather.R
import com.alexeykov.weather.adapters.CityListAdapter
import com.alexeykov.weather.databinding.FragmentMainBinding
import com.alexeykov.weather.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    private val mainViewModel: MainViewModel by viewModels()

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

        binding.pullToRefresh.setOnRefreshListener {
            mainViewModel.getForecast()
        }

        mainViewModel.setNavController(findNavController())

        observeCities()
    }

    private fun observeCities() {
        mainViewModel.items.observe(viewLifecycleOwner) {
            adapter.renderItems(it)
        }

        mainViewModel.delete.observe(viewLifecycleOwner) {
            if (!it.equals("")) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setMessage(R.string.dialog_delete)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        mainViewModel.deleteCity(it)
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        mainViewModel.clearDelete()
                        dialog.cancel()
                    }
                alertDialog.create().show()
            }
        }

        mainViewModel.errors.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

            binding.pullToRefresh.isRefreshing = false
        }

        mainViewModel.update.observe(viewLifecycleOwner) {
            binding.pullToRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}