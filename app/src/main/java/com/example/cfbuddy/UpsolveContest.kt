package com.example.cfbuddy

data class UpsolveContest(
    val contestId: Long,
    val contestName: String,
    val handle: String,
    val rank: Int,
    val ratingUpdateTimeSeconds: Long,
    val oldRating: Int,
    val newRating: Int
)
