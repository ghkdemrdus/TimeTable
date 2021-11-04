package com.luke.timetable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luke.timetable.data.SubjectTime
import com.luke.timetable.databinding.PickerSubjectTimeBinding

class PickerDialog() : DialogFragment() {

    private lateinit var binding: PickerSubjectTimeBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel() as T
        }).get(MainViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = PickerSubjectTimeBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context)

        val weeks = arrayOf("월", "화", "수", "목", "금")

        val hours = mutableListOf<String>()
        for (i in 9 until 21) {
            hours.add(String.format("%02d", i))
        }
        val minutes = mutableListOf<String>()
        for (i in 0 until 60 step 5) {
            minutes.add(String.format("%02d", i))
        }

        var day = "월"
        var startHour = "09"
        var startMinute = "00"
        var endHour = "10"
        var endMinute = "00"

        binding.apply {
            pickerDayOfWeek.apply {
                minValue = 0
                maxValue = 4
                displayedValues = weeks
                setOnValueChangedListener{ _, _, newVal ->
                    day = weeks[newVal]
                }
            }
            pickerStartHour.apply {
                minValue = 0
                maxValue = 11
                displayedValues = hours.toTypedArray()
                setOnValueChangedListener { _, _, newVal ->
                    startHour = hours[newVal]
                }
            }
            pickerStartMinute.apply {
                minValue = 0
                maxValue = 11
                displayedValues = minutes.toTypedArray()
                setOnValueChangedListener { _, _, newVal ->
                    startMinute = minutes[newVal]
                }
            }
            pickerEndHour.apply {
                minValue = 0
                maxValue = 11
                value = 1
                displayedValues = hours.toTypedArray()
                setOnValueChangedListener { _, _, newVal ->
                    endHour = hours[newVal]
                }
            }
            pickerEndMinute.apply {
                minValue = 0
                maxValue = 11
                displayedValues = minutes.toTypedArray()
                setOnValueChangedListener { _, _, newVal ->
                    endMinute = minutes[newVal]
                }
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
            btnConfirm.setOnClickListener {
                if(startHour.toFloat()+(startMinute.toFloat()/60) >= endHour.toFloat()+(endMinute.toFloat()/60)){
                    Toast.makeText(requireContext(), "시간을 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                } else{
                    mainViewModel.addSubjectTime(SubjectTime(dayToInt(day), "$startHour:$startMinute", "$endHour:$endMinute"))
                    dismiss()
                }
            }
        }
        builder.setView(binding.root)
        return builder.create()
    }
    fun dayToInt(string: String): Int{
        return when(string){
            "월" -> 0
            "화" -> 1
            "수" -> 2
            "목" -> 3
            else -> 4
        }
    }
}