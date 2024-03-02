package com.myungwoo.calender

data class CalendarVO(
    var date: String = "",
    var day: String = "",
    val isCurrentMonth: Boolean
)