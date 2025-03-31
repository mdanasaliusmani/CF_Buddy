package com.example.cfbuddy

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSet {

    @GET("contest.list?gym=false")
    suspend fun getUpcomingContests(): Response<ApiResponse<List<UpcomingContest>>>

    @GET("user.status?handle=Md_Anas_Ali_Usmani")
    suspend fun getUserStatus(): Response<ApiResponse<List<Submission>>>
}