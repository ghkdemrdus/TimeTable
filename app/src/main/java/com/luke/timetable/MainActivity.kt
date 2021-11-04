package com.luke.timetable

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luke.timetable.data.Subject
import com.luke.timetable.databinding.ActivityMainBinding
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    val bottomSheetDialog = SubjectAddFragment()
    lateinit var subjectDetailDialog : SubjectDetailDialog
    lateinit var sharedPreferences : SharedPreferences

    lateinit var header: LinearLayout
    lateinit var column: LinearLayout
    lateinit var row: LinearLayout
    lateinit var subjects: MutableList<Subject>

    var density = 0
    var rowHeight = 0f
    var rowWidth = 0f
    var scale = 48
    var idx = 0
    private var weeks = listOf(
        "월", "화", "수", "목", "금", "토", "일"
    )
    private var times = listOf(
        9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("timetable", Context.MODE_PRIVATE)
        idx = sharedPreferences.getInt("idx", 0)
        subjects = saveSubjects()
        density = ceil(resources.displayMetrics.density).toInt()

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                viewModel = mainViewModel
            }

        mainViewModel.apply {
            drawTimetable(subjects)
            setSubjectIdx(idx)
            setSubject(subjects)
            lists.observe(this@MainActivity, {
                subjects = lists.value!!
                drawColumn(subjects)
                loadSubjects(subjects)
            })
            openAddSubject.observe(this@MainActivity, {
                bottomSheetDialog.show(supportFragmentManager, "bottomSheet")
            })
            addSubjectIdx.observe(this@MainActivity, EventObserver{
                idx += 1
                sharedPreferences.edit {
                    putInt("idx", idx)
                }
            })
            navigateToSubjectListActivity.observe(this@MainActivity, EventObserver{
                val intent= Intent(this@MainActivity, SubjectListActivity::class.java).apply {
                    putParcelableArrayListExtra("lists", ArrayList(lists.value!!))
                }
                startActivity(intent)
            })
        }
    }

    fun saveSubjects(): MutableList<Subject> {
        val tmpLists = sharedPreferences.getString("subjectLists", "")
        return Gson().fromJson(tmpLists, object : TypeToken<MutableList<Subject>>() {}.type)
    }

    fun loadSubjects(subjects: MutableList<Subject>){
        sharedPreferences.edit {
            putString("subjectLists", Gson().toJson(subjects, object : TypeToken<MutableList<Subject>>() {}.type))
        }
    }

    private fun drawTimetable(subjects: MutableList<Subject>) {
        drawTimetableHeader()
        drawRow()
        drawColumn(subjects)
    }

    fun drawTimetableHeader() {
        header = findViewById(R.id.layout_header)
        header.setBackgroundResource(R.drawable.bg_round_top_border_12_timetable)
        val view = layoutInflater.inflate(R.layout.cell_left_top, header, false) as View
        header.addView(view)
        for (i in 0 until 5) {
            val tv = layoutInflater.inflate(R.layout.cell_week, header, false) as TextView
            tv.apply {
                text = weeks[i]
                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_line_left_border_timetable)
            }
            header.addView(tv)
        }
    }

    fun drawRow() {
        row = findViewById(R.id.layout_row)
        for (i in 0 until 11) {
            val ll = layoutInflater.inflate(R.layout.layout_timetable_row, row, false) as LinearLayout
            ll.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (density * scale)
            )
            if (i == 10) {
                ll.setBackgroundResource(R.drawable.bg_round_bottom_border_12_timetable)
            } else {
                ll.setBackgroundResource(R.drawable.bg_border_row_timetable)
            }

            val tv = layoutInflater.inflate(R.layout.cell_time, row, false) as TextView
            tv.apply {
                text = times[i].toString()
                gravity = Gravity.TOP or Gravity.RIGHT
            }
            ll.addView(tv)
            for (j in 0 until 5) {
                val view1 = layoutInflater.inflate(R.layout.cell_grid, row, false) as View
                view1.setBackgroundResource(R.drawable.bg_line_left_border_timetable)
                ll.addView(view1)
            }
            row.addView(ll)
        }
        row.viewTreeObserver.addOnGlobalLayoutListener {
            rowWidth = row.measuredWidth.toFloat()
            rowHeight = row.height.toFloat()
        }

    }

    fun drawColumn(list: List<Subject>) {
        column = findViewById(R.id.layout_column)
        column.removeAllViews()
        for (i in 0 until 5) {
            var startTime = 9f
            val tmpList = classifySubject(list, i)
            val ll = layoutInflater.inflate(R.layout.layout_timetable_column, column, false) as LinearLayout
            for (j in tmpList.indices){
                drawDummy(ll, (density*scale*(timeToFloat(tmpList[j].startTime)-startTime)).toInt())
                startTime = timeToFloat(tmpList[j].startTime)
                drawSubject(ll, (density*scale*(timeToFloat(tmpList[j].endTime)-startTime)).toInt(), tmpList[j])
                startTime = timeToFloat(tmpList[j].endTime)
            }
            column.addView(ll)
        }
    }

    fun drawDummy(linearLayout: LinearLayout, height: Int){
        val view = layoutInflater.inflate(R.layout.cell_subject, linearLayout, false) as TextView
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        linearLayout.addView(view)
    }
    fun drawSubject(linearLayout: LinearLayout, height: Int, subject: Subject){
        val view = layoutInflater.inflate(R.layout.cell_subject, linearLayout, false) as TextView
        view.apply{
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height-density*2)
            background = view.context.getDrawable(R.drawable.bg_round_border_subject_color_67)
            setText(subject.name+"\n"+subject.professor)
            setBackgroundResource(setColor(subject.color))
            setOnClickListener {
                subjectDetailDialog = SubjectDetailDialog(subject)
                subjectDetailDialog.show(supportFragmentManager, "detailSubject")
            }
        }
        linearLayout.addView(view)
        val line = layoutInflater.inflate(R.layout.cell_subject_bottom, linearLayout, false) as View
        line.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, density*2)
        linearLayout.addView(line)
    }

    fun classifySubject(list:List<Subject>, day: Int): List<Subject>{
        var tmpList = mutableListOf<Subject>()
        for(i in list.indices){

            if(list[i].day == day){
                tmpList.add(list[i])
            }
        }
        return sortSubject(tmpList)
    }

    fun sortSubject(list: List<Subject>): List<Subject>{
        return list.sortedBy { timeToFloat(it.startTime) }
    }

    fun timeToFloat(time : String): Float{
        val timeSplit = time.split(":")
        return timeSplit[0].toFloat()+timeSplit[1].toFloat()/60
    }

    fun dptopx(dp: Int): Float {
        val metrics = resources.displayMetrics
        return dp * ((metrics.densityDpi.toFloat()) / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun setColor(idx : Int) : Int{
        return when(idx){
            0 -> R.drawable.bg_round_border_subject_color_1
            1 -> R.drawable.bg_round_border_subject_color_3
            2 -> R.drawable.bg_round_border_subject_color_5
            3 -> R.drawable.bg_round_border_subject_color_10
            4 -> R.drawable.bg_round_border_subject_color_13
            5 -> R.drawable.bg_round_border_subject_color_17
            6 -> R.drawable.bg_round_border_subject_color_7
            7 -> R.drawable.bg_round_border_subject_color_12
            8 -> R.drawable.bg_round_border_subject_color_35
            9 -> R.drawable.bg_round_border_subject_color_69
            else -> R.drawable.bg_round_border_subject_color_67
        }
    }
}