package com.luke.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luke.timetable.databinding.ActivityMainBinding
import com.luke.timetable.databinding.FragmentAddSubjectBinding

class AddSubjectFragment() : BottomSheetDialogFragment(){

    private lateinit var binding: FragmentAddSubjectBinding
    private val pickerDialog = PickerDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_subject,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        val day = arguments?.getString("day")
        val startHour = arguments?.getString("startHour")
        val startMinute = arguments?.getString("startMinute")
        val endHour = arguments?.getString("endHour")
        val endMinute = arguments?.getString("endMinute")

        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.apply {
            btnSelectTime.setOnClickListener {
                pickerDialog.show(activity?.supportFragmentManager!!, "dialog")
            }
            tv4.text = day
            tv5.text = startHour
            tv6.text = startMinute
            tv7.text = endHour
            tv8.text = endMinute
        }

        // 드래그해도 팝업이 종료되지 않도록
//        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//        })


    }
}