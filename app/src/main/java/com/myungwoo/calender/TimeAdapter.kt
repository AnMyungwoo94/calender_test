package com.myungwoo.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeAdapter : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    // 00시부터 23시까지의 시간을 담은 리스트
    private val times: List<String> = (0..23).map { it.toString().padStart(2, '0') }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_list, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(times[position])

    }

    override fun getItemCount() = times.size

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView: TextView = itemView.findViewById(R.id.day)

        fun bind(time: String) {
            timeTextView.text = time
        }
    }
}