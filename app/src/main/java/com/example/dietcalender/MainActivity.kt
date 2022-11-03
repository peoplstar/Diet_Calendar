package com.example.dietcalender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dietcalender.databinding.ActivityMainBinding
import com.google.type.DateTime
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        .add(R.id.frameLayout, mainView())
        transaction.commit()

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
        val monthText: TextView = view.findViewById(R.id.tv_month)
        val dateText: TextView = view.findViewById(R.id.tv_date)
        val dayText: TextView = view.findViewById(R.id.tv_day)

        lateinit var day: CalendarDay

        init {
            Log.d("태그", ":inited ")
            view.setOnClickListener {
                val str = day.date.dayOfMonth.toString()
                Log.d("태그", "clicked $str" )
            }
        }

        // 날짜에 따라 textview에 값을 bind해줌
        fun bind(day: CalendarDay) {
            Log.d("태그", "bind ")
            this.day = day
            monthText.text = day.date.monthValue.toString() + '월'
            dateText.text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            dayText.text = day.date.dayOfMonth.toString()
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
