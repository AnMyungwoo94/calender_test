package com.myungwoo.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.databinding.ItemCalendarListBinding
import java.time.LocalDate

class CalendarAdapter(private val cList: List<Week>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var isItemsVisible: Boolean = true
    private var selectedPosition = -1

    fun toggleItemsVisibility() {
        isItemsVisible = !isItemsVisible
        notifyDataSetChanged()
    }

    class CalendarViewHolder(private val binding: ItemCalendarListBinding, val onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Week, isItemsVisible: Boolean, isSelected: Boolean) {
            val dates = listOf(item.day1, item.day2, item.day3, item.day4, item.day5, item.day6, item.day7)
            val textViews = listOf(binding.date, binding.date2, binding.date3, binding.date4, binding.date5, binding.date6, binding.date7)

            dates.zip(textViews).forEach { (day, textView) ->
                textView.text = day.day
                textView.alpha = if (day.isCurrentMonth) 1.0f else 0.5f
            }

            val today = LocalDate.now()
            dates.forEachIndexed { index, day ->
                val date = LocalDate.of(day.year.toInt(), day.month.toInt(), day.day.toInt())
                if (date.isEqual(today)) {
                    textViews[index].setBackgroundResource(R.drawable.bg_oval_orange)
                    textViews[index].setTextColor(getColor(binding.root.context, R.color.white))
                }
            }

            if (isItemsVisible) {
                binding.weekGroup.visibility = View.GONE
            } else {
                binding.weekGroup.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                // 선택 상태 토글
                it.isSelected = !it.isSelected
                // 필요한 경우 추가적인 UI 업데이트나 데이터 처리

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // onCreateViewHolder 내에서 onClick 람다를 정의하고 CalendarViewHolder에 전달합니다.
        return CalendarViewHolder(binding) { position ->
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(cList[position], isItemsVisible, isSelected)
    }


    override fun getItemCount(): Int {
        return cList.size
    }

}