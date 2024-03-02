package com.myungwoo.calender

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.myungwoo.calender.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    lateinit var calendarAdapter: CalendarAdapter
    private var calendarList = ArrayList<CalendarVO>()

    companion object {
        fun newInstance() = CalendarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var week_day: Array<String> = resources.getStringArray(R.array.calendar_day)

        calendarAdapter = CalendarAdapter(calendarList)

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 MM월").withLocale(Locale.forLanguageTag("ko"))

            val now = LocalDateTime.now()
            val localDate = now.format(monthFormat)
            binding.textYearMonth.text = localDate

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

        binding.weekRecycler.adapter = calendarAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}