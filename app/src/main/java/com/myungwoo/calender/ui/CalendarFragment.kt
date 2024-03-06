package com.myungwoo.calender.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.myungwoo.calender.adapter.CalendarAdapter
import com.myungwoo.calender.data.Day
import com.myungwoo.calender.adapter.TimeAdapter
import com.myungwoo.calender.data.Week
import com.myungwoo.calender.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private var calendarList = ArrayList<Week>()
    private var isItemsVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentMonth()
        setCurrentToday()
        setCalendarAdapter()
        binding.timeRecycler.adapter = TimeAdapter()
        setWeekVisible()
        setClickableAreasListeners()
        showModalBottomSheet()

    }

    private fun setCurrentMonth() {
        val now = LocalDateTime.now()
        val monthFormat = DateTimeFormatter.ofPattern("MM월").withLocale(Locale.forLanguageTag("ko"))
        binding.textYearMonth.text = now.format(monthFormat)

        val firstDay = now.with(TemporalAdjusters.firstDayOfMonth())
        val lastDay = now.with(TemporalAdjusters.lastDayOfMonth())
        var currentDate = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

        calendarList.clear()
        while (currentDate.isBefore(lastDay) || currentDate.isEqual(lastDay)) {
            val days = mutableListOf<Day>()
            for (i in 0 until 7) {
                val isCurrentMonth = currentDate.monthValue == now.monthValue
                days.add(Day(currentDate.year.toString(), currentDate.monthValue.toString(), currentDate.dayOfMonth.toString(), isCurrentMonth))
                currentDate = currentDate.plusDays(1)
            }
            if (days.size == 7) {
                calendarList.add(Week(days[0], days[1], days[2], days[3], days[4], days[5], days[6]))
            }
        }
    }

    private fun setCurrentToday() {
        val todayIndex = calendarList.indexOfFirst { week ->
            LocalDate.now().let { today ->
                today.isAfter(LocalDate.of(week.day1.year.toInt(), week.day1.month.toInt(), week.day1.day.toInt()).minusDays(1)) &&
                        today.isBefore(LocalDate.of(week.day7.year.toInt(), week.day7.month.toInt(), week.day7.day.toInt()).plusDays(1))
            }
        }

        if (todayIndex >= 0) {
            binding.weekRecycler.post {
                binding.weekRecycler.scrollToPosition(todayIndex)
            }
        }
    }

    private fun setCalendarAdapter() {
        calendarAdapter = CalendarAdapter(calendarList)
        binding.weekRecycler.adapter = calendarAdapter
        PagerSnapHelper().attachToRecyclerView(binding.weekRecycler)
    }

    private fun setWeekVisible() {
        binding.textView2.setOnClickListener {
            isItemsVisible = !isItemsVisible
            calendarAdapter.toggleItemsVisibility()
            if (isItemsVisible) {
                binding.group.visibility = View.VISIBLE
            } else {
                binding.group.visibility = View.GONE
            }
        }
    }

    private fun showModalBottomSheet() {
        binding.textYearMonth.setOnClickListener {
            val action = CalendarFragmentDirections.actionCalendarFragmentToModalBottomSheet("d")
            findNavController().navigate(action)
        }
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
                val visiblePosition = (binding.weekRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val currentWeek = calendarList.getOrNull(visiblePosition)
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
                clickedDay?.let { day ->
                    val action = CalendarFragmentDirections.actionCalendarFragmentToDailyFagment(clickedDay.toString())
                    findNavController().navigate(action)
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