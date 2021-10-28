package com.luke.timetable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment

class PickerDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val dialog = layoutInflater.inflate(R.layout.picker_subject_time, null)
        val weeks = arrayOf("월", "화", "수", "목", "금")
        val dayPicker = dialog.findViewById<NumberPicker>(R.id.picker_day_of_week)

        dayPicker.apply {
            minValue = 0
            maxValue = 4
            displayedValues = weeks
        }

        builder.setView(dialog)
        return builder.create()
    }
}