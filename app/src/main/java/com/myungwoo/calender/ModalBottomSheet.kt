package com.myungwoo.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.myungwoo.calender.databinding.Example1FragmentBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class ModalBottomSheet : BottomSheetDialogFragment() {
    private var _binding: Example1FragmentBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ModalBottomSheetArgs>()


    // 달력에 사용될 날짜들
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Example1FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val daysOfWeek = daysOfWeek()
        configureLegend(daysOfWeek)
        val currentMonth = YearMonth.now()
        setupMonthCalendar(currentMonth, daysOfWeek)
        setupWeekCalendar(currentMonth, daysOfWeek)

        // 달력의 월 스크롤 리스너 설정
        binding.exOneCalendar.monthScrollListener = {
            // 현재 보이는 달로 헤더의 년도와 월을 업데이트합니다.
            binding.exOneYearText.text = "${it.yearMonth.year}년 ${it.yearMonth.monthValue}월"
        }
    }

    private fun configureLegend(daysOfWeek: List<DayOfWeek>) {
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].name.take(1)
                textView.setTextColorRes(R.color.black)
            }
    }

    private fun setupMonthCalendar(currentMonth: YearMonth, daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView: TextView = view.findViewById(R.id.exOneDayText)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate && day.date !in selectedDates) {
                        selectedDates.add(day.date)
                        textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                    } else {
                        selectedDates.remove(day.date)
                        textView.background = null
                    }
                }
            }
        }

        binding.exOneCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                container.textView.setOnClickListener {
                    val action = ModalBottomSheetDirections.actionModalBottomSheetToCalendarFragment(container.textView.text.toString())
                    findNavController().navigate(action)
                }

                if (data.date in selectedDates) {
                    container.textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                } else {
                    container.textView.background = null
                }
            }
        }

        binding.exOneCalendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
        binding.exOneCalendar.scrollToMonth(currentMonth)
    }


    private fun setupWeekCalendar(currentMonth: YearMonth, daysOfWeek: List<DayOfWeek>) {
        // 이 부분은 월간 달력 설정과 유사하며, 주간 달력에 맞게 조정됩니다.
        // WeekCalendarView 설정 코드 여기에 추가
    }

    private fun TextView.setTextColorRes(colorRes: Int) {
        val color = context.getColor(colorRes)
        this.setTextColor(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}