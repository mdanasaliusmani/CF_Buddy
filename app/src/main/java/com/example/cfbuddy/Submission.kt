package com.example.cfbuddy

data class Submission(
    val id: Int,
    val contestId: Int,
    val creationTimeSeconds: Long,
    val problem: Problem,
    val verdict: String?
)
