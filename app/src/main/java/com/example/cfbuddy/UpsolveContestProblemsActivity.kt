package com.example.cfbuddy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cfbuddy.databinding.ActivityUpsolveContestProblemsBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UpsolveContestProblemsActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityUpsolveContestProblemsBinding
    private lateinit var UpsolveContestProblemsAdapter: UpsolveContestProblemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityUpsolveContestProblemsBinding.inflate(layoutInflater)
        val handles = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString(
            "user_handle",
            null
        ) ?: "Md_Anas_Ali_Usmani"
        enableEdgeToEdge()
        setContentView(bindings.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.upsolve_contest_problems)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val contestId = intent.getLongExtra("contestId", 2042)
        setupRecyclerView()

        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUpsolveContestProblems(contestId, handles)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                bindings.tvContestName.text = response.body()!!.result.contest.name
                val row = response.body()!!.result.rows.firstOrNull() ?: return@launch

                val problems = response.body()!!.result.problems
                val rows = response.body()!!.result.rows

                val out = problems.mapIndexed { index, problem ->
                    var status = "Not Attempted"
                    for (row in rows) {
                        val result = row.problemResults[index]
                        if (result.points > 0) {
                            status = "Accepted"
                            break
                        } else if (result.points == 0 && result.rejectedAttemptCount != 0) {
                            status = "Unsolved"
                            break
                        }
                    }

                    UpsolveProblemOutput(
                        contestId = problem.contestId,
                        name = problem.name,
                        rating = problem.rating,
                        status = status
                    )
                }

                UpsolveContestProblemsAdapter.upsolve_contest_problems = out.toMutableList()

            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }
    }

    private fun setupRecyclerView() = bindings.rvUnsolved.apply {
        UpsolveContestProblemsAdapter = UpsolveContestProblemsAdapter(this@UpsolveContestProblemsActivity)
        adapter = UpsolveContestProblemsAdapter
        layoutManager = LinearLayoutManager(this@UpsolveContestProblemsActivity)
    }
}