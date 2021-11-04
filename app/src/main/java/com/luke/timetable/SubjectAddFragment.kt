package com.luke.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luke.timetable.databinding.FragmentSubjectAddBinding

class SubjectAddFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSubjectAddBinding
    private val subjectAddTimeAdapter by lazy { SubjectAddTimeAdapter() }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel() as T
        }).get(MainViewModel::class.java)
    }
    private val pickerDialog = PickerDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject_add, container, false)
        binding.rvSubjectAddTime.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = subjectAddTimeAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SubjectAddFragment
            viewmodel = mainViewModel
        }

        mainViewModel.apply {
            timeLists.observe(this@SubjectAddFragment, {
                subjectAddTimeAdapter.updateLists(timeLists.value!!)
            })
            hideBottomSheet.observe(this@SubjectAddFragment, EventObserver {
                dismiss()
            })
            showEmptyCheckToast.observe(this@SubjectAddFragment, EventObserver{
                Toast.makeText(requireContext(), "모두 채워주세요", Toast.LENGTH_SHORT).show()
            })
            showTimeCheckToast.observe(this@SubjectAddFragment, EventObserver{
                Toast.makeText(requireContext(), "시간을 재설정해주세요", Toast.LENGTH_SHORT).show()
            })
            openTimeDialog.observe(this@SubjectAddFragment, EventObserver{
                pickerDialog.show(activity?.supportFragmentManager!!, "pickerDialog")
            })
            resetTimeLists.observe(this@SubjectAddFragment, EventObserver{
                subjectAddTimeAdapter.resetLists()
            })
        }
    }
}