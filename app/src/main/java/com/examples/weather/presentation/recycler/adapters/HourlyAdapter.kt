package com.examples.weather.presentation.recycler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examples.weather.databinding.HourlyItemBinding
import com.examples.weather.entities.Hourly
import com.examples.weather.presentation.fragments.DetailFragment.Companion.weatherCodeImages
import javax.inject.Inject

class HourlyAdapter @Inject constructor() : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private var data: Hourly? = null

    inner class HourlyViewHolder(val binding: HourlyItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            HourlyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 24

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourList = data?.hourly?.hour
        val temperatureList = data?.hourly?.temperature
        val weatherCodeList = data?.hourly?.weatherCode
        val timeFormated = hourList?.get(position)?.slice(11..12)
        val tempFormated = temperatureList?.get(position)?.toInt()?.toString() + "°C"
        with(holder.binding) {
            tvTemperature.text = tempFormated
            tvTime.text = timeFormated
            weatherCodeImages[weatherCodeList?.get(position)]?.let {
                imageView.setImageResource(it)
            }
            tvTemperature.visibility = View.VISIBLE
        }
    }

    fun setData(data: Hourly) {
        this.data = data
        notifyDataSetChanged()
    }
}