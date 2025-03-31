package com.example.cfbuddy

data class ApiResponse<T>(
    val status: String,
    val result: T
)
