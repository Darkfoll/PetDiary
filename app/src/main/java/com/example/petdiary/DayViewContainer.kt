package com.example.petdiary

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {

    val dayText: TextView =
        view.findViewById(R.id.dayText)

    val dayOfWeekText: TextView =
        view.findViewById(R.id.dayOfWeekText)
}