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
import com.examples.weather.app.WeatherApplication
import com.examples.weather.databinding.FragmentDetailBinding
import com.examples.weather.presentation.recycler.adapters.DetailAdapter
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.formatDay
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

    private val currentDate = Date()
    private val timeFormat = SimpleDateFormat("HH", Locale.getDefault())
    private val timeText: String = timeFormat.format(currentDate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            latitude = it?.getDouble(LATITUDE_KEY)
            longitude = it?.getDouble(LONGITUDE_KEY)
        }
        Log.d(TAG, "timeText = $timeText")
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
}