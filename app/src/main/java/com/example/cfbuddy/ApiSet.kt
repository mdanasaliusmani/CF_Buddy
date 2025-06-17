package com.example.cfbuddy

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSet {

    @GET("contest.list?gym=false")
    suspend fun getUpcomingContests(): Response<ApiResponse<List<UpcomingContest>>>

    @GET("user.status?")
    suspend fun getUserStatus(@Query("handle") handle: String): Response<ApiResponse<List<Submission>>>

    @GET("user.rating?")
    suspend fun getUpsolveContests(@Query("handle") handle: String): Response<ApiResponse<List<UpsolveContest>>>

    @GET("contest.standings?asManager=false&from=1&count=5&showUnofficial=true")
    suspend fun getUpsolveContestProblems(
        @Query("contestId") contestId: Long,
        @Query("handles") handles: String
    ): Response<ApiResponse<UpsolveProblem>>

    @GET("user.info?checkHistoricHandles=true")
    suspend fun getUserInfo(@Query("handles") handles: String): Response<ApiResponse<List<UserInfo>>>

}