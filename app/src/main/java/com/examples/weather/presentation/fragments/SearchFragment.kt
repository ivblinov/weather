package com.examples.weather.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.examples.weather.R
import com.examples.weather.app.WeatherApplication
import com.examples.weather.databinding.FragmentSearchBinding
import com.examples.weather.presentation.states.SearchState
import com.examples.weather.presentation.utils.checkValidCoordinate
import com.examples.weather.presentation.viewmodels.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LATITUDE_KEY = "latitudeKey"
const val LONGITUDE_KEY = "longitudeKey"

private const val TAG = "MyLog"

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: SearchViewModel

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity?.applicationContext as WeatherApplication).appComponent.inject(this)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.searchState.collect { state ->
                    when (state) {
                        SearchState.Loading -> {}
                        SearchState.Success -> {
                            val latitude = viewModel.coordinate?.latitude
                            val longitude = viewModel.coordinate?.longitude
                            navigateOnHomeFragment(latitude, longitude)
                        }
                    }
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            val locality = binding.locality.text.toString()
            if (locality.length > 1) {
                getAltitude(locality = locality)
            } else {
                Toast.makeText(requireContext(), "Введите более одного символа", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun getAltitude(locality: String) {
        viewModel.getAltitude(locality)
    }

    private fun navigateOnHomeFragment(latitude: Double?, longitude: Double?) {
        if (checkValidCoordinate(latitude, longitude)) {
            val bundle = bundleOf(
                Pair(LATITUDE_KEY, latitude),
                Pair(LONGITUDE_KEY, longitude),
            )
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment, bundle)
        }
    }

    companion object {
        val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}