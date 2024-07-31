package com.examples.weather.presentation.utils

fun checkValidCoordinate(latitude: Double?, longitude: Double?): Boolean {
    return latitude != null && longitude != null
}

fun formatDay(time: String?): String {
    var todayString = ""
    val day = "${time?.get(8)}${time?.get(9)}"
    val monthNumber = "${time?.get(5)}${time?.get(6)}"
    val month = monthMap[monthNumber]
    if (month != null) {
        todayString = "$month, $day"
    }
    return todayString
}

fun getHourFromDate(date: String): String = "${date[11]}${date[12]}"

val monthMap = mapOf(
    "01" to "Янв",
    "02" to "Фев",
    "03" to "Мар",
    "04" to "Апр",
    "05" to "Май",
    "06" to "Июн",
    "07" to "Июл",
    "08" to "Авг",
    "09" to "Сен",
    "10" to "Окт",
    "11" to "Ноя",
    "12" to "Дек",
)