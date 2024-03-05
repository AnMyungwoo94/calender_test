package com.myungwoo.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myungwoo.calender.databinding.FragmentDailyFagmentBinding
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class DailyFagment : Fragment() {

    private var _binding: FragmentDailyFagmentBinding? = null
    private val binding get() = _binding!!

    lateinit var dailyAdapter: DailyAdapter
    private var calendarList = ArrayList<CalendarVO>()

    lateinit var detailAdaper: DetailAdapter
    private var detailList = ArrayList<Detail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyFagmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var week_day: Array<String> = resources.getStringArray(R.array.calendar_day)
        dailyAdapter = DailyAdapter(calendarList)
        detailAdaper = DetailAdapter(detailList)

        calendarList.apply {
            val dateFormat =
                DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat =
                DateTimeFormatter.ofPattern("MM월").withLocale(Locale.forLanguageTag("ko"))

            val now = LocalDateTime.now()
            val localDate = now.format(monthFormat)
            binding.textMonth.text = localDate

            val firstDayOfMonth = now.withDayOfMonth(1)
            val lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())
            val preSunday = firstDayOfMonth.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
            var currentDate = preSunday
            while (currentDate.isBefore(lastDayOfMonth) || currentDate.isEqual(lastDayOfMonth)) {
                for (i in 0 until 7) {
                    // 해당 날짜가 현재 월에 속하는지 여부를 확인합니다.
                    val isCurrentMonth = currentDate.month == now.month
                    // 달력 데이터를 추가합니다.
                    add(CalendarVO(currentDate.format(dateFormat), week_day[i], isCurrentMonth))
                    currentDate = currentDate.plusDays(1)
                }
            }
        }

        detailList.apply {
            var hour = 0 // 시작 시간: 01:00
            val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()) // 시간 형식 지정

            while (hour <= 23) { // 종료 시간: 23:00
                val timeString = String.format("%02d:00", hour) // 시간을 문자열로 변환
                add(Detail(timeString)) // Detail 객체 생성 후 리스트에 추가
                hour++ // 1시간씩 증가
            }

        }

        binding.recyclerDaily.adapter = dailyAdapter
        binding.viewDailyDatail.adapter = detailAdaper

        detailAdaper.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}