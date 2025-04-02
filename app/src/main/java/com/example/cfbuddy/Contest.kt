package com.example.cfbuddy

data class Contest(
    val id: Int,
    val name: String,
    val type: String,
    val phase: String,
    val frozen: Boolean,
    val durationSeconds: Int,
    val startTimeSeconds: Long,
    val relativeTimeSeconds: Int
)
