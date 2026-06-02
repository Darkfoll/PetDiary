package com.example.petdiary

import android.net.Uri
import java.time.LocalDate

data class DiaryEntry(

    val text: String,

    val imageUri: Uri?,

    val time: String,

    val date: LocalDate
)