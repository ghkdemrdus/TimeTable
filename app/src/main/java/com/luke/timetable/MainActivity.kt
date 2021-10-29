package com.luke.timetable

import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.luke.timetable.databinding.ActivityMainBinding
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    lateinit var header: LinearLayout
    lateinit var column: LinearLayout
    lateinit var row: LinearLayout
    var density = 0
    var rowHeight = 0f
    var rowWidth = 0f
    var scale = 48

    private var weeks = listOf(
        "월", "화", "수", "목", "금", "토", "일"
    )
    private var times = listOf(
        9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7
    )
    private var subjects = mutableListOf<Subject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        density = ceil(resources.displayMetrics.density).toInt()

        val bottomSheetDialog = AddSubjectFragment()

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                viewModel = mainViewModel
            }
        drawTimetable()
        binding.btnAdd.setOnClickListener {
            bottomSheetDialog.show(supportFragmentManager, "tag")
        }

    }

    private fun drawTimetable() {
        makeDataset()
        drawTimetableHeader()
        drawRow()
        drawColumn(subjects)

    }

    private fun makeDataset() {
        subjects.add(Subject("소프트웨어공학", "14:00", "16:00", 1, 1))
        subjects.add(Subject("소프트웨어공학", "14:00", "16:00", 3, 1))
        subjects.add(Subject("사랑의 실천", "12:00", "13:30", 1, 3))
        subjects.add(Subject("사랑의 실천", "12:00", "13:30", 3, 3))
        subjects.add(Subject("인공지능", "10:30", "12:30", 0, 2))
        subjects.add(Subject("인공지능", "15:00", "17:00", 4, 2))
        subjects.add(Subject("컴파일러", "14:00", "16:00", 0, 4))
        subjects.add(Subject("컴파일러", "17:00", "19:00", 2, 4))
        subjects.add(Subject("라틴아메리카", "13:00", "15:00", 2, 5))
        subjects.add(Subject("라틴아메리카", "9:00", "10:30", 4, 5))
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
//                setTextColor(resources.getColor(R.color.black, theme))
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
//            ll.setBackgroundColor(Color.BLUE)
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
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height-density*2)
        view.background = view.context.getDrawable(R.drawable.bg_round_border_subject_color_67)
//        val marginParam = linearLayout.layoutParams as ViewGroup.MarginLayoutParams
//        marginParam.setMargins(0,0,0,-100)
//        marginParam.bottomMargin = dptopx(10).toInt()
//        view.layoutParams = marginParam
        view.setText(subject.name)
        view.setBackgroundResource(setColor(subject.color))
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