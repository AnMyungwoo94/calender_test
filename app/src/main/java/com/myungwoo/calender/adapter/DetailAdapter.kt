package com.myungwoo.calender.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myungwoo.calender.data.Detail
import com.myungwoo.calender.R
import com.myungwoo.calender.databinding.ItemDailyListBinding
import com.myungwoo.calender.ui.SetEventFragment

class DetailAdapter(private val dList: List<Detail>) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemDailyListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Detail) {
            binding.tvTime.text = item.time

            itemView.setOnClickListener {
                Toast.makeText(binding.root.context, "Detail Test", Toast.LENGTH_SHORT).show()
                Log.d("Detail Test", "time : ${item.time}")
                Log.d("Detail Test", "time")

                val setEventFragment = SetEventFragment()
                val fragmentManager =
                    (binding.root.context as FragmentActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.FragmentContainerView, setEventFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDailyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dList[position])
    }
}