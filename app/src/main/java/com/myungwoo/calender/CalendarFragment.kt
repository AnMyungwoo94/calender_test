package com.myungwoo.calender

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private var calendarList = ArrayList<Week>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 달의 연도와 월을 표시합니다.
        val now = LocalDateTime.now()
        val monthFormat = DateTimeFormatter.ofPattern("MM월").withLocale(Locale.forLanguageTag("ko"))
        val localDate = now.format(monthFormat)
        binding.textYearMonth.text = localDate

        // 이번 달의 첫째 날과 마지막 날을 구합니다.
        val firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())

        // 이번 달의 첫 주의 일요일부터 시작합니다.
        var currentDate = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

        calendarList.clear() // 기존 리스트를 클리어합니다.
        while (currentDate.isBefore(lastDayOfMonth) || currentDate.isEqual(lastDayOfMonth)) {
            val days = mutableListOf<Day>()
            // 한 주의 날짜를 수집합니다.
            for (i in 0 until 7) {
                val isCurrentMonth = currentDate.monthValue == now.monthValue
                days.add(Day(currentDate.year.toString(), currentDate.monthValue.toString(), currentDate.dayOfMonth.toString(), isCurrentMonth))
                currentDate = currentDate.plusDays(1)
            }
            // 수집된 날짜로 Week 객체를 생성하여 리스트에 추가합니다.
            if (days.size == 7) {
                calendarList.add(Week(days[0], days[1], days[2], days[3], days[4], days[5], days[6]))
            }

        }

        calendarAdapter = CalendarAdapter(calendarList)
        binding.weekRecycler.adapter = calendarAdapter


        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.weekRecycler)

        binding.textView2.setOnClickListener {
            binding.group.visibility = View.VISIBLE
        }

        binding.timeRecycler.adapter = TimeAdapter()

        setClickableAreasListeners()
    }

    private fun setClickableAreasListeners() {
        val clickableViews = listOf(
            binding.clickableArea1,
            binding.clickableArea2,
            binding.clickableArea3,
            binding.clickableArea4,
            binding.clickableArea5,
            binding.clickableArea6,
            binding.clickableArea7
        )

        clickableViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                // 현재 보이는 주의 인덱스를 계산합니다.
                val visiblePosition = (binding.weekRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                // 현재 주를 가져옵니다.
                val currentWeek = calendarList.getOrNull(visiblePosition)
                // 클릭된 날짜를 가져옵니다.
                val clickedDay = currentWeek?.let { week ->
                    when (index) {
                        0 -> week.day1
                        1 -> week.day2
                        2 -> week.day3
                        3 -> week.day4
                        4 -> week.day5
                        5 -> week.day6
                        6 -> week.day7
                        else -> null
                    }
                }
                // 로그를 출력합니다.
                clickedDay?.let { day ->
                    Log.d("CalendarFragment", "Clicked on area ${index + 1}, Date: ${day.year}-${day.month}-${day.day}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}