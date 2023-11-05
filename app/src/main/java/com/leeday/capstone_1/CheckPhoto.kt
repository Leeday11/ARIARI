// CheckPhotoFragment.kt
package com.leeday.capstone_1

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.JsonArray
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CheckPhoto : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var textViewDate: TextView
    private lateinit var apiService: ApiService
    private lateinit var globalVariable: GlobalVariable

    companion object {
        private const val EXTRA_CREATE_DATE = "EXTRA_CREATE_DATE"
        private const val EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH"

        fun newInstance(createDate: String, imagePath: String): CheckPhoto {
            val fragment = CheckPhoto()
            val args = Bundle()
            args.putString(EXTRA_CREATE_DATE, createDate)
            args.putString(EXTRA_IMAGE_PATH, imagePath)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.check_photo, container, false)
        imageView = view.findViewById(R.id.checkPhotoView)
        textViewDate = view.findViewById(R.id.dateTextView)
        globalVariable = activity?.application as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        arguments?.let { bundle ->
            val createDate = bundle.getString(EXTRA_CREATE_DATE)
            val imagePath = bundle.getString(EXTRA_IMAGE_PATH)

            // 'createDate'를 파싱하고 원하는 형식으로 포맷합니다.
            createDate?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MM월 dd일의 기록", Locale.getDefault())
                try {
                    val date = inputFormat.parse(createDate)
                    date?.let {
                        val formattedDate = outputFormat.format(date)
                        textViewDate.text = formattedDate
                    } ?: run {
                        textViewDate.text = "Unknown Date"
                    }
                } catch (e: ParseException) {
                    textViewDate.text = "Unknown Date"
                }
            } ?: run {
                textViewDate.text = "Unknown Date"
            }

            imagePath?.let { loadImage(it) }
        }

        return view
    }

    private fun loadImage(imageName: String) {
        apiService.getPhotoFile(imageName, "Bearer " + globalVariable.accesstoken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // InputStream을 Bitmap으로 변환합니다
                        val inputStream = response.body()?.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        // ImageView에 Bitmap을 설정합니다
                        imageView.setImageBitmap(bitmap)
                    } else {
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
    }
}
