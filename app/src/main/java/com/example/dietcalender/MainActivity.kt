package com.example.dietcalender

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.dietcalender.databinding.ActivityMainBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val TAG = "TAG"
    var before: TextView? = null
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        binding.mainBtn.setOnClickListener {
            replaceFragment()
        }


        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val daysOfWeek = arrayOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )

        binding.calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        binding.calendarView.scrollToDate(LocalDate.now())

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                container.bind(day)

            }
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        var now: LocalDate = LocalDate.now()
        var year: String = now.format(DateTimeFormatter.ofPattern("yyyy"))
        lateinit var after: TextView
        private val monthText = view.findViewById<TextView>(R.id.tv_month)
        private val dateText = view.findViewById<TextView>(R.id.tv_date)
        private val dayText = view.findViewById<TextView>(R.id.tv_day)

        lateinit var day: CalendarDay

        init {
            view.setOnClickListener {
                before?.setTextColor(Color.parseColor("#ffffff"))
                it.findViewById<TextView>(R.id.tv_day).apply {
                    this.setTextColor(Color.parseColor("#f3b369"))
                    before = this
                }
                val click_day = "$year-${monthText.text.dropLast(1)}-${dayText.text}"
                Log.d(TAG, ": $click_day")

                val transaction = supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, mainView())
                transaction.commit()
            }
        }

        // 날짜에 따라 textview에 값을 bind해줌
        fun bind(day: CalendarDay) {
            this.day = day
            monthText.text = day.date.monthValue.toString() + '월'
            dateText.text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            dayText.text = day.date.dayOfMonth.toString()
            //before = dayText

            if (now == day.date) {
                dayText.setTextColor(Color.parseColor("#f3b369")) // 시작 시 색 변경
                before = dayText
            }
        }
    }

    /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragment)
        ...
        그리고 사용할 프래그먼트만 show하고 나머지는 hide한다.

        transaction.show(fragmentToUse)
        transaction.hide(fragmentNotForUse)
        ...
        transaction.commit()
     */

    private fun replaceFragment() {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, mainView())
        transaction.commit()
    }
}