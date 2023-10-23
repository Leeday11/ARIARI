package com.leeday.capstone_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CheckGraph : AppCompatActivity() {

    private lateinit var frequencyChart: LineChart
    private lateinit var musclepainChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_graph)

        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        frequencyChart = findViewById(R.id.frequency_chart)
        musclepainChart = findViewById(R.id.musclepain_chart)

//        // 첫 번째 그래프 데이터 로드
//        apiService.getAnswers("Bearer " + globalVariable.accesstoken, answer).enqueue(object : Callback<JsonArray> {
//            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
//                if (response.isSuccessful) {
//                    val jsonArray = response.body()
//
//                    if (jsonArray != null && jsonArray.size() > 0) {
//                        val jsonObject = jsonArray[0].asJsonObject
//
//                        val answer = Answer(
//                            jsonObject.get("flushing_face").asInt,
//                            jsonObject.get("sweating").asInt,
//                            jsonObject.get("headache").asInt,
//                            jsonObject.get("condition").asInt
//                        )
//
//                        val labels = listOf("Flushing Face", "Sweating", "Headache", "Condition")
//                        val values = listOf(answer.flushing_face, answer.sweating, answer.headache, answer.condition)
//                        val entries = values.mapIndexed { index, value ->
//                            Entry(index.toFloat(), value.toFloat())
//                        }
//
//                        val dataSet = LineDataSet(entries, "Symptoms over Time")
//                        dataSet.setDrawValues(false)
//                        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
//
//                        val lineData = LineData(dataSet)
//                        frequencyChart.data = lineData
//                        frequencyChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
//                        frequencyChart.invalidate()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
//                Log.e("API_ERROR", t.message ?: "Unknown error")
//            }
//        })

        // 두 번째 그래프 데이터 로드
        apiService.getAnswers2("Bearer " + globalVariable.accesstoken)
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val jsonArray = response.body()
                        val totalCount = jsonArray?.size() ?: 0

                        // 각 부위별 카운트 초기화
                        var shoulderCount = 0
                        var elbowCount = 0
                        var fingerCount = 0
                        var wristCount = 0
                        var jointCount = 0
                        var kneeCount = 0
                        var ankleCount = 0
                        var waistCount = 0

                        // JsonArray를 순회하며 누적값 계산
                        for (i in 0 until totalCount) {
                            val jsonObject = jsonArray?.get(i)?.asJsonObject
                            if (jsonObject?.get("shoulder")?.asBoolean == true) shoulderCount++
                            if (jsonObject?.get("elbow")?.asBoolean == true) elbowCount++
                            if (jsonObject?.get("finger")?.asBoolean == true) fingerCount++
                            if (jsonObject?.get("wrist")?.asBoolean == true) wristCount++
                            if (jsonObject?.get("joint")?.asBoolean == true) jointCount++
                            if (jsonObject?.get("knee")?.asBoolean == true) kneeCount++
                            if (jsonObject?.get("ankle")?.asBoolean == true) ankleCount++
                            if (jsonObject?.get("waist")?.asBoolean == true) waistCount++
                        }

                        // PieEntry 리스트 생성
                        val pieEntries = listOf(
                            PieEntry(shoulderCount.toFloat(), "Shoulder"),
                            PieEntry(elbowCount.toFloat(), "Elbow"),
                            PieEntry(fingerCount.toFloat(), "Finger"),
                            PieEntry(wristCount.toFloat(), "Wrist"),
                            PieEntry(jointCount.toFloat(), "Joint"),
                            PieEntry(kneeCount.toFloat(), "Knee"),
                            PieEntry(ankleCount.toFloat(), "Ankle"),
                            PieEntry(waistCount.toFloat(), "Waist")
                        )

                        // 사용자 정의 색상 배열 (16진수 색상 코드 사용)
                        val customColors = listOf(
                            Color.parseColor("#F7A3A7"),  // 빨간색
                            Color.parseColor("#F7AD97"),  // 주황색
                            Color.parseColor("#FAD89E"),  // 노란색
                            Color.parseColor("#C8D7C4"),  // 초록색
                            Color.parseColor("#BBCBD2"),  // 파란색
                            Color.parseColor("#B7B6D6"),  // 보라색
                            Color.parseColor("#E2BBD8"),  // 분홍색
                            Color.parseColor("#DED6D4")   // 회색
                        )
                        // 파이차트 데이터셋 생성 및 적용
                        val pieDataSet = PieDataSet(pieEntries, "Pain Distribution")

                        // 사용자 정의 색상 사용
                        pieDataSet.colors = customColors

                        val pieData = PieData(pieDataSet)
                        musclepainChart.data = pieData
                        musclepainChart.invalidate()
                    }
                }
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.e("API_ERROR", t.message ?: "Unknown error")
                }
            })
    }
}