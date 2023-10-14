package com.leeday.capstone_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class CheckGraph : AppCompatActivity() {

    private lateinit var frequencyChart: LineChart
    private lateinit var musclepainChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_graph)

        frequencyChart = findViewById(R.id.frequency_chart)
        musclepainChart = findViewById(R.id.musclepain_chart)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.200.84.39/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // 첫 번째 그래프 데이터 로드
        apiService.getSymptoms().enqueue(object : Callback<List<Symptom>> {
            override fun onResponse(call: Call<List<Symptom>>, response: Response<List<Symptom>>) {
                if (response.isSuccessful) {
                    val symptoms = response.body()
                    val entries = symptoms?.mapIndexed { index, symptom ->
                        Entry(index.toFloat(), symptom.flushing_face.toFloat())
                    }
                    val dataSet = LineDataSet(entries, "Flushing Face")
                    val lineData = LineData(dataSet)
                    frequencyChart.data = lineData
                    frequencyChart.invalidate()
                }
            }

            override fun onFailure(call: Call<List<Symptom>>, t: Throwable) {
                // Handle error
            }
        })

        // 두 번째 그래프 데이터 로드
        apiService.getSymptoms().enqueue(object : Callback<List<Symptom>> {
            override fun onResponse(call: Call<List<Symptom>>, response: Response<List<Symptom>>) {
                if (response.isSuccessful) {
                    val symptoms = response.body()
                    val entries = symptoms?.mapIndexed { index, symptom ->
                        Entry(index.toFloat(), symptom.sweating.toFloat())
                    }
                    val dataSet = LineDataSet(entries, "Sweating")
                    val lineData = LineData(dataSet)
                    musclepainChart.data = lineData
                    musclepainChart.invalidate()
                }
            }

            override fun onFailure(call: Call<List<Symptom>>, t: Throwable) {
                // Handle error
            }
        })
    }
}
