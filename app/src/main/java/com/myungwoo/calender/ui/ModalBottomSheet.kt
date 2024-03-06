package com.myungwoo.calender.ui

import android.os.Bundle
import android.util.Log
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
import com.myungwoo.calender.R
import com.myungwoo.calender.databinding.Example1FragmentBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class ModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: Example1FragmentBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ModalBottomSheetArgs>()
    private val selectedDates = mutableSetOf<LocalDate>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Example1FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDate = args.day
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()

        configureLegend(daysOfWeek)
        setupMonthCalendar(currentMonth, daysOfWeek, selectedDate)

        binding.exOneCalendar.monthScrollListener = {
            binding.exOneYearText.text = "${it.yearMonth.year}년 ${it.yearMonth.monthValue}월"
        }
    }

    private fun configureLegend(daysOfWeek: List<DayOfWeek>) {
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].name.take(1)
            }
    }

    private fun setupMonthCalendar(currentMonth: YearMonth, daysOfWeek: List<DayOfWeek>, selectedDate: String) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}