package com.myungwoo.calender

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.databinding.ItemCalendarListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarAdapter(private val cList: List<CalendarVO>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    init {
        setHasStableIds(true)
    }

    class CalendarViewHolder(private val binding: ItemCalendarListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarVO) {


            binding.date.text = item.date
            binding.day.text = item.day

            if (!item.isCurrentMonth) {
                binding.date.alpha = 0.5f
            } else {
                binding.date.alpha = 1.0f
            }


            val today = binding.date.text
            val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))
            if (today == now) {
                binding.date.setBackgroundResource(R.drawable.bg_oval_orange)
                binding.date.setTextColor(getColor(binding.root.context, R.color.white))
                binding.day.setTextColor(getColor(binding.root.context, R.color.orange))
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