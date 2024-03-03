package com.myungwoo.calender

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.databinding.ItemCalendarListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarAdapter(private val cList: List<Week>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemCalendarListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Week) {
            val dates = listOf(item.day1, item.day2, item.day3, item.day4, item.day5, item.day6, item.day7)
            // 아래 줄의 date2가 두 번 나오는 것을 수정합니다.
            val textViews = listOf(binding.date, binding.date2, binding.date3, binding.date4, binding.date5, binding.date6, binding.date7)

            dates.zip(textViews).forEach { (day, textView) ->
                // 날짜를 "월/일" 형식으로 설정합니다.
                textView.text = "${day.month}/${day.day}"
                textView.alpha = if (day.isCurrentMonth) 1.0f else 0.5f
            }

            // 오늘 날짜를 강조하는 로직을 수정합니다.
            // 현재 로직은 binding.date의 텍스트와 오늘 날짜를 비교하는데,
            // 이는 오늘 날짜에 해당하는 TextView를 찾아서 강조해야 합니다.
            val today = LocalDate.now()
            dates.forEachIndexed { index, day ->
                val date = LocalDate.of(day.year.toInt(), day.month.toInt(), day.day.toInt())
                if (date.isEqual(today)) {
                    textViews[index].setBackgroundResource(R.drawable.bg_oval_orange)
                    textViews[index].setTextColor(getColor(binding.root.context, R.color.white))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(cList[position])
    }

    override fun getItemCount(): Int {
        return cList.size
    }

}