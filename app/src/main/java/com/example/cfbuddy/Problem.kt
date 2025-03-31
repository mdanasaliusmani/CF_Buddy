package com.example.cfbuddy

data class Problem(
    val contestId: Int,
    val index: String,
    val name: String,
    val rating: Int?,
    val tags: List<String>
)
