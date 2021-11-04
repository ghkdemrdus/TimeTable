package com.luke.timetable.data

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subject(
    val idx : Int,
    val name : String,
    val professor: String,
    val content : String,
    val day : Int,
    val startTime : String,
    val endTime : String,
    val color: Int
): Parcelable
