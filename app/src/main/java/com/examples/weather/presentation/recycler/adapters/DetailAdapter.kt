package com.examples.weather.presentation.recycler.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examples.weather.databinding.DailyItemBinding
import com.examples.weather.entities.Daily
import com.examples.weather.presentation.fragments.HomeFragment.Companion.weatherCodeImages
import com.examples.weather.presentation.utils.formatDay
import javax.inject.Inject

private const val TAG = "MyLog"
class DetailAdapter @Inject constructor() : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var data: Daily? = null

    inner class DetailViewHolder(val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            DailyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val dayList = data?.daily?.day
        val temperatureList = data?.daily?.temperature
        val weatherCodeList = data?.daily?.weatherCode
        val dayFormated = formatDay(dayList?.get(position))
        val tempFormated = temperatureList?.get(position)?.toInt()?.toString() + "°"
        with(holder.binding) {
            tvDate.text = dayFormated
            weatherCodeImages[weatherCodeList?.get(position)]?.let {
                imageWeatherCode.setImageResource(
                    it
                )
            }
            tvTemperature.text = tempFormated
        }
    }

    fun setData(data: Daily) {
        this.data = data
        notifyDataSetChanged()
    }
}