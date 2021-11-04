package com.luke.timetable

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luke.timetable.data.Subject
import com.luke.timetable.data.SubjectTime

class MainViewModel(): ViewModel() {

    private var cnt = 5
    val title = MutableLiveData<String>("")
    val professor = MutableLiveData<String>("")
    var subjectLists = mutableListOf<Subject>()
    var subjectTimeLists = mutableListOf<SubjectTime>()

    private val _lists = MutableLiveData<MutableList<Subject>>()
    val lists: LiveData<MutableList<Subject>>
        get() = _lists
    private val _timeLists = MutableLiveData<MutableList<SubjectTime>>()
    val timeLists: LiveData<MutableList<SubjectTime>>
        get() = _timeLists

    private val _closeBottomSheet = MutableLiveData<String>()
    val closeBottomSheet: LiveData<String>
        get() = _closeBottomSheet

    //Event
    private val _showEmptyCheckToast = MutableLiveData<Event<Boolean>>()
    val showEmptyCheckToast: LiveData<Event<Boolean>>
        get() = _showEmptyCheckToast

    private val _showTimeCheckToast = MutableLiveData<Event<Boolean>>()
    val showTimeCheckToast: LiveData<Event<Boolean>>
        get() = _showTimeCheckToast

    private val _openAddSubject = MutableLiveData<Event<Boolean>>()
    val openAddSubject: LiveData<Event<Boolean>>
        get() = _openAddSubject

    private val _openTimeDialog = MutableLiveData<Event<Boolean>>()
    val openTimeDialog: LiveData<Event<Boolean>>
        get() = _openTimeDialog

    private val _hideBottomSheet = MutableLiveData<Event<Boolean>>()
    val hideBottomSheet: LiveData<Event<Boolean>>
        get() = _hideBottomSheet

    private val _addSubjectIdx = MutableLiveData<Event<Boolean>>()
    val addSubjectIdx: LiveData<Event<Boolean>>
        get() = _addSubjectIdx

    private val _resetTimeLists = MutableLiveData<Event<Boolean>>()
    val resetTimeLists: LiveData<Event<Boolean>>
        get() = _resetTimeLists

    private val _navigateToSubjectListActivity = MutableLiveData<Event<Boolean>>()
    val navigateToSubjectListActivity: LiveData<Event<Boolean>>
        get() = _navigateToSubjectListActivity

    fun openBottomSheet(){
        _openAddSubject.value = Event(true)
    }

    fun setSubject(lists: MutableList<Subject>){
        this.subjectLists = lists
        _lists.value = this.subjectLists
    }

    fun setSubjectIdx(idx : Int){
        cnt = idx
    }
    fun addSubjectTime(subjectTime: SubjectTime){
        subjectTimeLists.add(subjectTime)
        _timeLists.value = subjectTimeLists
    }

    fun addSubject(){
        if(title.value == "" || subjectTimeLists.size == 0){
            _showEmptyCheckToast.value = Event(true)
        } else if(!checkSubjects(subjectTimeLists)) {
            _showTimeCheckToast.value = Event(true)
        } else{
            val randInt = (0..10).random()
            for (timeList in subjectTimeLists){
                val list = Subject(cnt, title.value!!, professor.value!!,"비지정 (서울)", timeList.day,timeList.startTime, timeList.endTime, randInt)
                subjectLists.add(list)
            }
            cnt += 1
            Log.d("TAG", "$subjectLists")
            _lists.value = subjectLists
            title.value = ""
            subjectTimeLists = mutableListOf()
            _timeLists.value = mutableListOf()
            _hideBottomSheet.value = Event(true)
            _addSubjectIdx.value = Event(true)
            _resetTimeLists.value = Event(true)
        }

    }

    fun checkSubjects(subjectTimeLists: List<SubjectTime>): Boolean{
        for(timeList in subjectTimeLists){
            for(list in this.subjectLists){
                if(timeList.day == list.day){
                    if(timeToFloat(list.startTime) >= timeToFloat(timeList.startTime) && timeToFloat(list.startTime) < timeToFloat(timeList.endTime)){
                        return false
                    }
                    if(timeToFloat(list.endTime) > timeToFloat(timeList.startTime) && timeToFloat(list.endTime) <= timeToFloat(timeList.endTime)){
                        return false
                    }
                }
            }
        }
        return true
    }
    fun timeToFloat(time : String): Float{
        val timeSplit = time.split(":")
        return timeSplit[0].toFloat()+timeSplit[1].toFloat()/60
    }
    fun removeSubject(subject: Subject){
        val tmpLists = mutableListOf<Subject>()
        for (list in subjectLists){
            if(subject.idx != list.idx){
                tmpLists.add(list)
            }
        }
        subjectLists = tmpLists
        _lists.value = subjectLists
    }

    fun navigateToSubjectListActivity(){
        _navigateToSubjectListActivity.value = Event(true)
    }

    fun addTime(){
        _openTimeDialog.value = Event(true)
    }

}