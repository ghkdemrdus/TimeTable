package com.luke.timetable

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luke.timetable.data.Subject
import com.luke.timetable.data.SubjectTime
import com.luke.timetable.databinding.DialogSubjectDatailBinding

class SubjectDetailDialog(var subject: Subject): DialogFragment() {
    private lateinit var binding: DialogSubjectDatailBinding
    private val subjectDetailTimeAdapter by lazy { SubjectDetailTimeAdapter() }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel() as T
        }).get(MainViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSubjectDatailBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            rvDialogSubjectTime.adapter = subjectDetailTimeAdapter
            subjectDetailTimeAdapter.setLists(setTime(subject.idx))
            dialogSubjectTitle.text = subject.name
            dialogSubjectProfessor.text = subject.professor
            dialogSubjectContent.text = subject.content
            btn1.setOnClickListener {
                dismiss()
            }
            btn2.setOnClickListener {
                mainViewModel.removeSubject(subject)
                dismiss()
            }
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun setTime(idx: Int): List<SubjectTime>{
        val timeLists = mutableListOf<SubjectTime>()
        for(list in mainViewModel.lists.value!!){
            if(list.idx == idx){
                timeLists.add(SubjectTime(list.day, list.startTime, list.endTime))
            }
        }
        return sortSubject(timeLists)
    }
    fun sortSubject(list: List<SubjectTime>): List<SubjectTime>{
        return list.sortedBy { it.day }
    }
}