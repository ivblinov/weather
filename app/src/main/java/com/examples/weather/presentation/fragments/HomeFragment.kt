package com.examples.weather.presentation.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.examples.weather.R
import com.examples.weather.app.WeatherApplication
import com.examples.weather.databinding.FragmentHomeBinding
import com.examples.weather.presentation.fragments.SearchFragment.Companion.REQUIRED_PERMISSIONS
import com.examples.weather.presentation.viewmodels.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import javax.inject.Inject

private const val TAG = "MyLog"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var fusedClient: FusedLocationProviderClient? = null
    private var cancellationSource: CancellationTokenSource? = null
    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity?.applicationContext as WeatherApplication).appComponent.inject(this)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
        cancellationSource = CancellationTokenSource()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedClient?.let { fusedClient ->
            cancellationSource?.let { cancellationSource ->
                checkPermissions(fusedClient, cancellationSource)
            }
        }

        binding.btnDetail.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cancellationSource?.cancel()
    }

    private fun checkPermissions(
        fusedClient: FusedLocationProviderClient,
        cancellationSource: CancellationTokenSource
    ) {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startLocation(fusedClient, cancellationSource)
        } else {
            Log.d(TAG, "checkPermissions: false")
        }
    }

    private fun startLocation(
        fusedClient: FusedLocationProviderClient,
        cancellationSource: CancellationTokenSource
    ) {
        try {
            val result = fusedClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationSource.token
            )
            result.addOnSuccessListener {
                viewModel.getWeather(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                "Необходимо разрешить использовать геолокацию.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}