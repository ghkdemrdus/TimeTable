package com.luke.timetable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luke.timetable.data.Subject
import com.luke.timetable.data.SubjectTime

class SubjectDetailTimeAdapter: RecyclerView.Adapter<SubjectDetailTimeAdapter.ViewHolder>() {

    private var lists = listOf<SubjectTime>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item: SubjectTime){
            itemView.findViewById<TextView>(R.id.tv_day).text = "${intToDay(item.day)}요일"
            itemView.findViewById<TextView>(R.id.tv_start_time).text = item.startTime
            itemView.findViewById<TextView>(R.id.tv_end_time).text = item.endTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_detail, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lists[position])
    }
    override fun getItemCount(): Int = lists.size

    fun setLists(lists: List<SubjectTime>) {
        this.lists = lists
        notifyDataSetChanged()
    }

    fun intToDay(day: Int): String{
        return when(day){
            0 -> "월"
            1 -> "화"
            2 -> "수"
            3 -> "목"
            else -> "금"
        }
    }
}