package com.luke.timetable

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.luke.timetable.data.Subject
import com.luke.timetable.databinding.ActivitySubjectListBinding

class SubjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectListBinding
    private val subjectListViewModel : SubjectListViewModel by viewModels()
    private val subjectListAdapter by lazy { SubjectListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_list)
        binding.apply {
            viewModel = subjectListViewModel
            rvSubjectList.apply {
                layoutManager = LinearLayoutManager(this@SubjectListActivity, LinearLayoutManager.VERTICAL, false)
                adapter = subjectListAdapter
            }
        }
        subjectListAdapter.updateLists(intent.getParcelableArrayListExtra<Subject>("lists")!!)

    }
}