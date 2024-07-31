package com.examples.weather.presentation.fragments

import android.os.Bundle
import android.util.Log
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
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.formatDay
import com.examples.weather.presentation.utils.getHourFromDate
import com.examples.weather.presentation.viewmodels.DetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


private const val TAG = "MyLog"

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double? = null
    private var longitude: Double? = null

    @Inject
    lateinit var viewModel: DetailViewModel

    @Inject
    lateinit var dayAdapter: DetailAdapter

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
                                val weatherCodeList = viewModel.hourly?.hourly?.weatherCode
                                val temperatureList = viewModel.hourly?.hourly?.temperature
                                val hourList = viewModel.hourly?.hourly?.hour

                                val temp1 = startIndex?.let { temperatureList?.get(it) }?.toInt()
                                if (temp1 != null) {
                                    val fulTemp1 = "$temp1°C"
                                    binding.tempItem1.text = fulTemp1
                                }

                                val temp2 =
                                    startIndex?.let { temperatureList?.get(it + 1) }?.toInt()
                                if (temp2 != null) {
                                    val fulTemp2 = "$temp2°C"
                                    binding.tempItem2.text = fulTemp2
                                }

                                val temp3 =
                                    startIndex?.let { temperatureList?.get(it + 2) }?.toInt()
                                if (temp3 != null) {
                                    val fulTemp3 = "$temp3°C"
                                    binding.tempItem3.text = fulTemp3
                                }

                                val temp4 =
                                    startIndex?.let { temperatureList?.get(it + 3) }?.toInt()
                                if (temp4 != null) {
                                    val fulTemp4 = "$temp4°C"
                                    binding.tempItem4.text = fulTemp4
                                }

                                val temp5 =
                                    startIndex?.let { temperatureList?.get(it + 4) }?.toInt()
                                if (temp5 != null) {
                                    val fulTemp5 = "$temp5°C"
                                    binding.tempItem5.text = fulTemp5
                                }

                                val weatherCode1 = startIndex?.let { weatherCodeList?.get(it) }
                                weatherCodeImages[weatherCode1]?.let {
                                    binding.imageItem1.setImageResource(
                                        it
                                    )
                                }

                                val weatherCode2 = startIndex?.let { weatherCodeList?.get(it + 1) }
                                weatherCodeImages[weatherCode2]?.let {
                                    binding.imageItem2.setImageResource(
                                        it
                                    )
                                }

                                val weatherCode3 = startIndex?.let { weatherCodeList?.get(it + 2) }
                                weatherCodeImages[weatherCode3]?.let {
                                    binding.imageItem3.setImageResource(
                                        it
                                    )
                                }

                                val weatherCode4 = startIndex?.let { weatherCodeList?.get(it + 3) }
                                weatherCodeImages[weatherCode4]?.let {
                                    binding.imageItem4.setImageResource(
                                        it
                                    )
                                }

                                val weatherCode5 = startIndex?.let { weatherCodeList?.get(it + 4) }
                                weatherCodeImages[weatherCode5]?.let {
                                    binding.imageItem5.setImageResource(
                                        it
                                    )
                                }

                                val time1 = startIndex?.let { hourList?.get(it) }
                                if (time1 != null) {
                                    val fullTime1 = "${getHourFromDate(time1)}.00"
                                    binding.timeItem1.text = fullTime1
                                }

                                val time2 = startIndex?.let { hourList?.get(it + 1) }
                                if (time2 != null) {
                                    val fullTime2 = "${getHourFromDate(time2)}.00"
                                    binding.timeItem2.text = fullTime2
                                }

                                val time3 = startIndex?.let { hourList?.get(it + 2) }
                                if (time3 != null) {
                                    val fullTime3 = "${getHourFromDate(time3)}.00"
                                    binding.timeItem3.text = fullTime3
                                }

                                val time4 = startIndex?.let { hourList?.get(it + 3) }
                                if (time4 != null) {
                                    val fullTime4 = "${getHourFromDate(time4)}.00"
                                    binding.timeItem4.text = fullTime4
                                }

                                val time5 = startIndex?.let { hourList?.get(it + 4) }
                                if (time5 != null) {
                                    val fullTime5 = "${getHourFromDate(time5)}.00"
                                    binding.timeItem5.text = fullTime5
                                }
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