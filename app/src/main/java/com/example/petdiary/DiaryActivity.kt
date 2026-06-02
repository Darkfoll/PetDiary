package com.example.petdiary

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
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
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.compose.remote.creation.first
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageButton
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.net.Uri
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.ImageView

class DiaryActivity : AppCompatActivity() {

    private lateinit var weekCalendarView: WeekCalendarView
    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner

    private lateinit var openSheetBtn: Button
    private var isUpdatingFromCalendar = false

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: DiaryAdapter

    private val allEntries = mutableListOf<DiaryEntry>()

    private val visibleEntries = mutableListOf<DiaryEntry>()

    private var selectedImageUri: Uri? = null

    private lateinit var selectedImageView: ImageView
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
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
        monthSpinner = findViewById(R.id.monthSpinner)
        yearSpinner = findViewById(R.id.yearSpinner)

        setupMonthYearPickers()
        setupCalendar()

        recyclerView = findViewById(R.id.entriesRecyclerView)

        adapter = DiaryAdapter(visibleEntries)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        openSheetBtn = findViewById(R.id.openSheetBtn)

        val backBtn = findViewById<Button>(R.id.backCard)

        backBtn.setOnClickListener {


            finish() // закрывает текущее окно
        }

        openSheetBtn.setOnClickListener {
            showBottomSheet()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateEntriesForSelectedDate() {

        visibleEntries.clear()

        visibleEntries.addAll(

            allEntries.filter {

                it.date == selectedDate
            }
        )

        adapter.notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.O)
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

        weekCalendarView.weekScrollListener = { week ->

            val visibleDate = week.days[3].date

            isUpdatingFromCalendar = true

            monthSpinner.setSelection(
                visibleDate.monthValue - 1,
                false
            )

            val yearPosition =
                (2020..2035).indexOf(visibleDate.year)

            yearSpinner.setSelection(
                yearPosition,
                false
            )

            monthSpinner.post {
                isUpdatingFromCalendar = false
            }
        }
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
                        updateEntriesForSelectedDate()
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthYearPickers() {

        val months = listOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )

        val years = (2020..2035).map { it.toString() }

        val monthAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            months
        )

        val yearAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            years
        )

        monthSpinner.adapter = monthAdapter
        yearSpinner.adapter = yearAdapter

        val currentDate = LocalDate.now()

        monthSpinner.setSelection(currentDate.monthValue - 1)
        yearSpinner.setSelection(years.indexOf(currentDate.year.toString()))

        val listener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (isUpdatingFromCalendar) return

                val selectedMonth =
                    monthSpinner.selectedItemPosition + 1

                val selectedYear =
                    yearSpinner.selectedItem.toString().toInt()

                val firstDayOfMonth =
                    LocalDate.of(selectedYear, selectedMonth, 1)

                weekCalendarView.scrollToDate(firstDayOfMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        monthSpinner.onItemSelectedListener = listener
        yearSpinner.onItemSelectedListener = listener
    }

    private val imagePickerLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                selectedImageUri = uri

                selectedImageView.setImageURI(uri)

                selectedImageView.visibility = View.VISIBLE
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showBottomSheet() {

        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(
            R.layout.bottom_sheet_pet,
            null
        )

        dialog.setContentView(view)

        val editText =
            view.findViewById<EditText>(R.id.editTextNote)

        val saveBtn =
            view.findViewById<Button>(R.id.saveBtn)

        val addImageBtn =
            view.findViewById<ImageButton>(R.id.addImageBtn)

        selectedImageView =
            view.findViewById(R.id.selectedImage)

        addImageBtn.setOnClickListener {

            imagePickerLauncher.launch("image/*")
        }

        saveBtn.setOnClickListener {

            val text =
                editText.text.toString().trim()

            // Проверка:
            // должен быть текст ИЛИ фото

            if (text.isEmpty() && selectedImageUri == null) {

                editText.error =
                    "Добавьте текст или фото"

                return@setOnClickListener
            }

            val currentTime =
                LocalTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )

            val entry = DiaryEntry(
                text = text,
                imageUri = selectedImageUri,
                time = currentTime,
                date = selectedDate
            )

            allEntries.add(0, entry)

            updateEntriesForSelectedDate()

            selectedImageUri = null

            dialog.dismiss()
        }

        dialog.show()
    }

}