package com.luke.timetable

import android.graphics.*
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.luke.timetable.databinding.ActivityMainBinding
import com.google.android.material.internal.ViewUtils.dpToPx


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    val row = 11
    val column = 6
    lateinit var tableBox: TableLayout

    private var weekList = listOf(
        "","월", "화", "수", "목", "금"
    )
    private var timeList = listOf(
        9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                viewModel = mainViewModel
            }

        binding.btn.setOnClickListener {
            timetableView()
        }
        tableBox = this.findViewById(R.id.tl)
        createTimetable()
//        val bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ALPHA_8)
//        val canvas = Canvas(bitmap)
//        val paint = Paint()
//        paint.color = Color.RED
//        canvas.drawLine(10f, 100f, 4000f, 400f, paint)

    }

    fun createTimetable() {
        createTimetableHeader()
        createTimetableBody()
    }

    fun createTimetableHeader() {
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        for (i in 0 until column) {
            val tv = TextView(this)
            tv.layoutParams = TableRow.LayoutParams(50, 50)
            tv.setText(weekList[i])
            tv.setTextColor(resources.getColor(R.color.black))
            tv.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            tv.layoutParams = TableRow.LayoutParams(50, 50)

            tableRow.addView(tv)
        }
        tableBox.addView(tableRow)

    }

    fun createTimetableBody() {
        for (i in 0 until row) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
            )
            for (j in 0 until column) {
                val tv = TextView(this)
                tv.layoutParams = TableRow.LayoutParams(50, 50)
                tv.setBackgroundResource(R.color.purple_200)
                if (j == 0) {
                    tv.setText(timeList[i].toString())
                    tv.setTextColor(resources.getColor(R.color.black))
                    tv.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    tv.layoutParams = TableRow.LayoutParams(50, 50)
                } else {
                    tv.text = ""
                    tv.gravity = Gravity.RIGHT
                    tv.layoutParams = TableRow.LayoutParams(50, 50)

                }
                tableRow.addView(tv)
            }
            tableBox.addView(tableRow)
        }
    }

    fun timetableView() {
        val l = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        l.layoutParams = params
        l.orientation = LinearLayout.HORIZONTAL

        //EditText view
        val e = EditText(applicationContext)
        val paramsEditText = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsEditText.weight = 1f
        e.layoutParams = paramsEditText
        e.hint = "Type new Task"

        //Image View for close button

        //Image View for close button
        val i = ImageView(applicationContext)
        val paramsImgView = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsImgView.weight = 0.15f
        i.background = resources.getDrawable(R.drawable.ic_launcher_background)

        //Adding the EditText and Close Button to LinearLayout

        //Adding the EditText and Close Button to LinearLayout
        l.addView(e)
        l.addView(i)
        l.removeAllViews()
        l.addView(e)
        l.addView(i)

        //Adding the Created LinearLayout to Container

        //Adding the Created LinearLayout to Container
        val finalParent = this.findViewById(R.id.container) as ViewGroup

        finalParent.addView(l)

        i.setOnClickListener {
            val parent = i.parent as ViewGroup
            parent.removeAllViews()
            if (parent.parent != null) {
                (parent.parent as ViewGroup).removeView(parent)
            }
        }
        //Adding OnClick Listener to Close button Image for removing the view

        //Adding OnClick Listener to Close button Image for removing the view

    }

    fun createXmlElement(title: String, description: String) {
        val parent = LinearLayout(this)
        parent.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        parent.orientation = LinearLayout.HORIZONTAL


        //children of parent linearlayout
        val iv = ImageView(this)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        lp.setMargins(0, 11, 7, 0)
        iv.setLayoutParams(lp)
        iv.setImageResource(R.drawable.ic_launcher_background)
        iv.getLayoutParams().height = 40
        iv.getLayoutParams().width = 46


        parent.addView(iv); // lo agregamos al layout

        val relativeP = RelativeLayout(this)
        relativeP.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        val linearCH = LinearLayout(this)
        linearCH.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        // TextView1
        val tv1 = TextView(this)
        val lptv1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        lptv1.setMargins(0, 7, 0, 0)

        tv1.setLayoutParams(lptv1)
        tv1.setText(title) // title
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
        tv1.setTypeface(null, Typeface.BOLD)

        // TextView2
        val tv2 = TextView(this)
        val lptv2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        lptv2.setMargins(0, 11, 7, 0)

        tv2.setLayoutParams(lptv1)
        tv2.setText(description) // description
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
        tv2.setTypeface(null, Typeface.BOLD)

        linearCH.removeAllViews()
        linearCH.addView(tv1)
        linearCH.addView(tv2)

        relativeP.removeAllViews()
        relativeP.addView(linearCH)

        // last ImageView
        val iv2 = ImageView(this)
        val lpiv2 = RelativeLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        lpiv2.setMargins(0, 11, 7, 0)
        lpiv2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        iv2.setLayoutParams(lpiv2)
        iv2.setImageResource(R.drawable.ic_launcher_background)
        iv2.getLayoutParams().height = 40
        iv2.getLayoutParams().width = 46


        parent.removeAllViews()
        parent.addView(iv)
        parent.addView(relativeP)
        parent.addView(iv2)

        val finalParent = this.findViewById(R.id.cl1) as ViewGroup

        finalParent.addView(parent)
    }
}