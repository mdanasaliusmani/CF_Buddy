package com.example.cfbuddy

data class ProblemResult(
    val points: Int,
    val rejectedAttemptCount: Int,
    val type: String,
    val bestSubmissionTimeSeconds: Long? = null  // Optional field, as it may be absent
)
