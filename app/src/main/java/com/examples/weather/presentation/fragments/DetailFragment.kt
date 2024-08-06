package com.examples.weather.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.examples.weather.R
import com.examples.weather.app.WeatherApplication
import com.examples.weather.databinding.FragmentDetailBinding
import com.examples.weather.presentation.recycler.adapters.DetailAdapter
import com.examples.weather.presentation.recycler.adapters.HourlyAdapter
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.formatDay
import com.examples.weather.presentation.utils.getHourFromDate
import com.examples.weather.presentation.viewmodels.DetailViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double? = null
    private var longitude: Double? = null

    @Inject
    lateinit var viewModel: DetailViewModel

    @Inject
    lateinit var dayAdapter: DetailAdapter

    @Inject
    lateinit var hourlyAdapter: HourlyAdapter

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
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDaily(latitude, longitude)
        viewModel.getHourly(latitude, longitude)

        binding.dayRecycler.adapter = dayAdapter
        binding.hourlyRecycler.adapter = hourlyAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.detailState.collect { state ->
                        when (state) {
                            HomeState.Loading -> {}
                            HomeState.Success -> {
                                val time = viewModel.daily?.daily?.day?.first()
                                val todayString = formatDay(time)
                                binding.tvTodayResult.text = todayString

                                with(viewModel.daily?.daily) {
                                    this?.day?.removeFirstOrNull()
                                    this?.temperature?.removeFirstOrNull()
                                    this?.weatherCode?.removeFirstOrNull()
                                }
                                viewModel.daily?.let { dayAdapter.setData(it) }
                            }
                        }
                    }
                }
                launch {
                    viewModel.hourlyState.collect { state ->
                        when (state) {
                            HomeState.Loading -> {}
                            HomeState.Success -> {
                                val startIndex = viewModel.startIndex
                                startIndex?.let { startIndexLocal ->
                                    with(viewModel.hourly?.hourly) {
                                        repeat(startIndexLocal) {
                                            this?.temperature?.removeFirstOrNull()
                                            this?.weatherCode?.removeFirstOrNull()
                                            this?.hour?.removeFirstOrNull()
                                        }
                                    }
                                    viewModel.hourly?.let { hourlyAdapter.setData(it) }
                                }
                                binding.hourlyRecycler.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        binding.blackBlock.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.hourlyRecycler.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
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
    }
}