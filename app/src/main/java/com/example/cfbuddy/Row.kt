package com.example.cfbuddy

data class Row(
    val party: Party,
    val rank: Int,
    val points: Int,
    val penalty: Int,
    val successfulHackCount: Int,
    val unsuccessfulHackCount: Int,
    val problemResults: List<ProblemResult>
)

