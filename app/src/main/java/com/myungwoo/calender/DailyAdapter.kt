package com.myungwoo.calender

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.CalendarVO
import com.myungwoo.calender.databinding.ItemCalendarListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DailyAdapter(private val cList: List<CalendarVO>) :
    RecyclerView.Adapter<DailyAdapter.CalendarViewHolder>() {

    init {
        setHasStableIds(true)
    }

    class CalendarViewHolder(private val binding: ItemCalendarListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var selectedItemPosition = RecyclerView.NO_POSITION

        fun bind(item: CalendarVO) {
            binding.date.text = item.date
            binding.day.text = item.day

            if (!item.isCurrentMonth) {
                binding.date.alpha = 0.5f
            } else {
                binding.date.alpha = 1.0f
            }

            val today = binding.date.text
            val now = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))


            itemView.setOnClickListener {
                Toast.makeText(binding.root.context, "recycler test ", Toast.LENGTH_SHORT).show()
                Log.d("Calendar Test", "date : ${item.date}")
                Log.d("Calendar Test", "date : ${item.day}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(cList[position])
    }

    override fun getItemCount(): Int {
        return cList.size
    }
}