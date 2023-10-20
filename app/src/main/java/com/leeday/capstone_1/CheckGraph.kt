package com.leeday.capstone_1

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

    // Answer2 객체를 입력으로 받아 PieEntry 리스트를 반환하는 함수
    fun createPieEntriesFromAnswer(answer: Answer2): List<PieEntry> {
        // 각 부위별로 true 값을 카운트
        val shoulderCount = if (answer.shoulder) 1 else 0
        val elbowCount = if (answer.elbow) 1 else 0
        val fingerCount = if (answer.finger) 1 else 0
        val wristCount = if (answer.wrist) 1 else 0
        val jointCount = if (answer.joint) 1 else 0
        val kneeCount = if (answer.knee) 1 else 0
        val ankleCount = if (answer.ankle) 1 else 0
        val waistCount = if (answer.waist) 1 else 0

        // PieEntry 리스트 생성
        return listOf(
            PieEntry(shoulderCount.toFloat(), "Shoulder"),
            PieEntry(elbowCount.toFloat(), "Elbow"),
            PieEntry(fingerCount.toFloat(), "Finger"),
            PieEntry(wristCount.toFloat(), "Wrist"),
            PieEntry(jointCount.toFloat(), "Joint"),
            PieEntry(kneeCount.toFloat(), "Knee"),
            PieEntry(ankleCount.toFloat(), "Ankle"),
            PieEntry(waistCount.toFloat(), "Waist")
        )
    }

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


    // 첫 번째 그래프 데이터 로드
        apiService.getAnswers("Bearer " + globalVariable.accesstoken).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    Log.d("tlqkf",response.body().toString())

                    val answer = response.body()?.let {
                        Answer(
                            it.get("flushing_face").asInt,
                            it.get("sweating").asInt,
                            it.get("headache").asInt,
                            it.get("condition").asInt
                        )
                    }

                    if (answer != null) {
                        val labels = listOf("Flushing Face", "Sweating", "Headache", "Condition")
                        val values = listOf(answer.flushing_face, answer.sweating, answer.headache, answer.condition)

                        val entries = values.mapIndexed { index, value ->
                            Entry(index.toFloat(), value.toFloat())
                        }

                        val dataSet = LineDataSet(entries, "Symptoms over Time")
                        dataSet.setDrawValues(false) // 값 표시 여부를 설정. 필요에 따라 설정을 바꿀 수 있습니다.
                        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS) // 그래프에 색상을 지정합니다.

                        val lineData = LineData(dataSet)
                        frequencyChart.data = lineData
                        frequencyChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels) // x축 라벨을 설정
                        frequencyChart.invalidate()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("API_ERROR", t.message ?: "Unknown error")

            }
        })

        apiService.getAnswers2("Bearer " + globalVariable.accesstoken).enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()

                    // JsonArray를 활용하여 데이터 처리

                    if (jsonArray != null) {
                        val shoulder = jsonArray.get(0).asBoolean
                        val elbow = jsonArray.get(1).asBoolean
                        val finger = jsonArray.get(2).asBoolean
                        val wrist = jsonArray.get(3).asBoolean
                        val joint = jsonArray.get(4).asBoolean
                        val knee = jsonArray.get(5).asBoolean
                        val ankle = jsonArray.get(6).asBoolean
                        val waist = jsonArray.get(7).asBoolean

                        val answerData = Answer2(shoulder, elbow, finger, wrist, joint, knee, ankle, waist)

                        // Answer2 객체를 사용해 PieEntry 리스트 생성
                        val pieEntries = createPieEntriesFromAnswer(answerData)

                        val pieDataSet = PieDataSet(pieEntries, "Pain Distribution")
                        val pieData = PieData(pieDataSet)
                        musclepainChart.data = pieData
                        musclepainChart.invalidate()
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("API_ERROR", t.message ?: "Unknown error")
            }
        })


        // 두 번째 그래프 데이터 로드
//        apiService.getAnswers2("Bearer " + globalVariable.accesstoken).enqueue(object : Callback<JsonArray> {
//            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
//                if (response.isSuccessful) {
//                    val symptomData = response.body()
//                    val answerData = Answer2(
//                        symptomData?.get("shoulder")?.asBoolean ?: false,
//                        symptomData?.get("elbow")?.asBoolean ?: false,
//                        symptomData?.get("finger")?.asBoolean ?: false,
//                        symptomData?.get("wrist")?.asBoolean ?: false,
//                        symptomData?.get("joint")?.asBoolean ?: false,
//                        symptomData?.get("knee")?.asBoolean ?: false,
//                        symptomData?.get("ankle")?.asBoolean ?: false,
//                        symptomData?.get("waist")?.asBoolean ?: false
//                    )
//
//                    // Answer2 객체를 사용해 PieEntry 리스트 생성
//                    val pieEntries = createPieEntriesFromAnswer(answerData)
//
//                    val pieDataSet = PieDataSet(pieEntries, "Pain Distribution")
//                    val pieData = PieData(pieDataSet)
//                    musclepainChart.data = pieData
//                    musclepainChart.invalidate()
//                }
//            }
//
//            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
//                Log.e("API_ERROR", t.message ?: "Unknown error")
//            }
//        })


    }
}
