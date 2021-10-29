package com.luke.timetable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.luke.timetable.databinding.FragmentAddSubjectBinding
import com.luke.timetable.databinding.PickerSubjectTimeBinding

class PickerDialog : DialogFragment() {

    private lateinit var binding: PickerSubjectTimeBinding

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
                val bundle = Bundle()
                bundle.apply {
                    putString("day", day)
                    putString("startHour", startHour)
                    putString("startMinute", startMinute)
                    putString("endHour", endHour)
                    putString("endMinute", endMinute)
                }
                val addSubjectFragment = AddSubjectFragment()
                addSubjectFragment.arguments = bundle
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(R.id.bottomSheetDashBoardLayout, addSubjectFragment)
                    .commit()
            }
        }
        builder.setView(binding.root)
        return builder.create()
    }

}