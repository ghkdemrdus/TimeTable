package com.luke.timetable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luke.timetable.data.Subject

class SubjectListAdapter() : RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {

    private var lists = mutableListOf<Subject>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Subject) {
            itemView.findViewById<TextView>(R.id.tv_title).text = item.name
            itemView.findViewById<TextView>(R.id.tv_time).text = item.startTime

            itemView.setOnClickListener {
                Log.d("TAG", "itemClick")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_subject_list, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    fun updateLists(lists: MutableList<Subject>) {
        this.lists = lists
        notifyDataSetChanged()
    }
}