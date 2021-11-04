package com.luke.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luke.timetable.data.Subject

class SubjectListViewModel: ViewModel() {

    var lists = mutableListOf<Subject>()
    private val _day = MutableLiveData<String>("월요일")
    val day: LiveData<String>
        get() = _day
}