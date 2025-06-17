package com.example.cfbuddy

data class Party(
    val contestId: Int,
    val members: List<Member>,
    val participantType: String,
    val ghost: Boolean,
    val startTimeSeconds: Long
)
