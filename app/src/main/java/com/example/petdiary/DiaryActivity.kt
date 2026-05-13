package com.example.petdiary

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class DiaryActivity : AppCompatActivity() {

    private lateinit var weekCalendarView: WeekCalendarView

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_diary)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        weekCalendarView = findViewById(R.id.weekCalendarView)

        setupCalendar()
    }

    private fun setupCalendar() {

        val currentDate = LocalDate.now()

        val startDate = currentDate.minusMonths(12)
        val endDate = currentDate.plusMonths(12)

        weekCalendarView.setup(
            startDate,
            endDate,
            DayOfWeek.MONDAY
        )

        weekCalendarView.scrollToDate(currentDate)

        weekCalendarView.dayBinder =
            object : WeekDayBinder<DayViewContainer> {

                override fun create(view: android.view.View): DayViewContainer {
                    return DayViewContainer(view)
                }

                override fun bind(
                    container: DayViewContainer,
                    data: WeekDay
                ) {

                    container.dayText.text =
                        data.date.dayOfMonth.toString()

                    container.dayOfWeekText.text =
                        data.date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale("ru")
                        ).uppercase()

                    if (data.date == selectedDate) {

                        val drawable = GradientDrawable()

                        drawable.shape = GradientDrawable.OVAL
                        drawable.setColor(Color.parseColor("#4A90E2"))

                        container.dayText.background = drawable

                        container.dayText.setTextColor(Color.WHITE)

                    } else {

                        container.dayText.background = null

                        container.dayText.setTextColor(Color.BLACK)
                    }

                    container.view.setOnClickListener {

                        selectedDate = data.date

                        weekCalendarView.notifyCalendarChanged()
                    }
                }
            }
    }
}