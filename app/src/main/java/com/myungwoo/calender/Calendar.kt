package com.myungwoo.calender

data class CalendarVO(
    var date: String = "",
    var day: String = "",
    val isCurrentMonth: Boolean
)

data class Day(
    var year: String,
    var month: String,
    var day: String,
    val isCurrentMonth: Boolean
)

data class Week(
    var day1: Day,
    var day2: Day,
    var day3: Day,
    var day4: Day,
    var day5: Day,
    var day6: Day,
    var day7: Day
)