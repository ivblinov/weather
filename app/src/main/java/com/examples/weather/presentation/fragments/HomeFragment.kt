package com.examples.weather.presentation.fragments

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.examples.weather.R
import com.examples.weather.app.WeatherApplication
import com.examples.weather.databinding.FragmentHomeBinding
import com.examples.weather.presentation.fragments.SearchFragment.Companion.REQUIRED_PERMISSIONS
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.checkValidCoordinate
import com.examples.weather.presentation.viewmodels.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

private const val TAG = "MyLog"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var fusedClient: FusedLocationProviderClient? = null
    private var cancellationSource: CancellationTokenSource? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            latitude = it?.getDouble(LATITUDE_KEY)
            longitude = it?.getDouble(LONGITUDE_KEY)
        }
    }

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

        startLocation(latitude, longitude)

        fusedClient?.let { fusedClient ->
            cancellationSource?.let { cancellationSource ->
                checkPermissions(fusedClient, cancellationSource)
            }
        }

        binding.btnDetail.setOnClickListener {
            navigateOnDetailFragment(latitude, longitude)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.currentWeatherState.collect { state ->
                        when (state) {
                            HomeState.Loading -> {
                                binding.blackBlock.visibility = View.GONE
                                binding.weatherBlock.visibility = View.GONE
                                binding.btnDetail.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            HomeState.Success -> {
                                val weather = viewModel.currentWeather
                                val temperature = weather?.temperature?.toInt()?.toString()
                                val weatherCode = weather?.weatherCode
                                val weatherCodeString = weatherCodeMap[weather?.weatherCode]
                                val windSpeed = weather?.windSpeed?.toInt()?.toString() ?: "0"
                                val windResultString = "$windSpeed км/ч"
                                val humidity = weather?.relativeHumidity ?: ""
                                val humidityResult = "$humidity%"
                                val time = weather?.time
                                val day = "${time?.get(8) ?: ""}${time?.get(9) ?: ""}"
                                val monthNumber = "${time?.get(5) ?: ""}${time?.get(6) ?: ""}"
                                val month = monthMap[monthNumber] ?: ""
                                val todayString = "Сегодня, $day $month"

                                if (weatherCode != null) {
                                    weatherCodeImages[weather.weatherCode]?.let {
                                        binding.imageView.setImageResource(
                                            it
                                        )
                                    }
                                }
                                with(binding) {
                                    tvTemperature.text = temperature
                                    tvDegree.visibility = View.VISIBLE
                                    tvCloudy.text = weatherCodeString
                                    windResult.text = windResultString
                                    humResult.text = humidityResult
                                    tvDateToday.text = todayString
                                }
                                if (weatherCode != null) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.blackBlock.visibility = View.VISIBLE
                                    binding.weatherBlock.visibility = View.VISIBLE
                                    binding.btnDetail.visibility = View.VISIBLE
                                } else {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                launch {
                    viewModel.geocoderState.collect { state ->
                        when (state) {
                            HomeState.Loading -> {

                            }
                            HomeState.Success -> {
                                binding.tvLocality.text = viewModel.address ?: ""
                                if (viewModel.address != null && viewModel.address != "") {
                                    binding.imageLocality.visibility = View.VISIBLE
                                } else {
                                    binding.imageLocality.visibility = View.GONE
                                    latitude?.let {
                                        longitude?.let { it1 ->
                                            viewModel.getNameLocality(
                                                it, it1, requireContext()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationSource.token
            )
            result.addOnSuccessListener {
                viewModel.getWeather(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    context = this.requireContext()
                )
                viewModel.getNameLocality(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    this.requireContext()
                )
                latitude = it.latitude
                longitude = it.longitude
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                "Необходимо разрешить использовать геолокацию.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startLocation(latitudeCoord: Double?, longitudeCoord: Double?) {
        if (latitudeCoord != null && longitudeCoord != null) {
            viewModel.getWeather(
                latitude = latitudeCoord,
                longitude = longitudeCoord,
                context = this.requireContext()
            )
            viewModel.getNameLocality(
                latitude = latitudeCoord,
                longitude = longitudeCoord,
                this.requireContext()
            )
        }
    }

    private fun navigateOnDetailFragment(latitude: Double?, longitude: Double?) {
        if (checkValidCoordinate(latitude, longitude)) {
            val bundle = bundleOf(
                Pair(LATITUDE_KEY, latitude),
                Pair(LONGITUDE_KEY, longitude),
            )
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }

    companion object {
        private val weatherCodeMap = mapOf(
            0 to "Чистое небо",
            1 to "Ясно",
            2 to "Облачно",
            3 to "Пасмурно",
            45 to "Туман",
            48 to "Изморозь",
            51 to "Слабая морось",
            53 to "Умеренная морось",
            55 to "Интенсивная морось",
            56 to "Слабая замерзающая морось",
            57 to "Сильная замерзающая морось",
            61 to "Слабый дождь",
            63 to "Дождь",
            65 to "Сильный дождь",
            66 to "Слабый замерзающий дождь",
            67 to "Сильный замерзающий дождь",
            71 to "Слабый снегопад",
            73 to "Снегопад",
            75 to "Сильный снегопад",
            77 to "Снежные зерна",
            80 to "Слабый ливневый дождь",
            81 to "Ливневый дождь",
            82 to "Сильный ливневый дождь",
            85 to "Слабый снежный ливень",
            86 to "Сильный снежный ливень",
            95 to "Гроза",
            96 to "Гроза с небольшим градом",
            99 to "Гроза с сильным градом",
        )

        val weatherCodeImages = mapOf(
            0 to R.drawable.image_sunny,
            1 to R.drawable.image_sunny,
            2 to R.drawable.image_sun_cloudy,
            3 to R.drawable.image_cloudy,
            45 to R.drawable.image_cloudy,
            48 to R.drawable.image_cloudy,
            51 to R.drawable.image_rain,
            53 to R.drawable.image_rain,
            55 to R.drawable.image_rain,
            56 to R.drawable.image_rain,
            57 to R.drawable.image_rain,
            61 to R.drawable.image_rain,
            63 to R.drawable.image_rain,
            65 to R.drawable.image_rain,
            66 to R.drawable.image_rain,
            67 to R.drawable.image_rain,
            71 to R.drawable.image_snow,
            73 to R.drawable.image_snow,
            75 to R.drawable.image_snow,
            77 to R.drawable.image_snow,
            80 to R.drawable.image_rain,
            81 to R.drawable.image_rain,
            82 to R.drawable.image_rain,
            85 to R.drawable.image_rain,
            86 to R.drawable.image_rain,
            95 to R.drawable.image_thunder,
            96 to R.drawable.image_thunder,
            99 to R.drawable.image_thunder,
        )

        private val monthMap = mapOf(
            "01" to "Января",
            "02" to "Февраля",
            "03" to "Марта",
            "04" to "Апреля",
            "05" to "Мая",
            "06" to "Июня",
            "07" to "Июля",
            "08" to "Августа",
            "09" to "Сентября",
            "10" to "Октября",
            "11" to "Ноября",
            "12" to "Декабря",
        )
    }
}