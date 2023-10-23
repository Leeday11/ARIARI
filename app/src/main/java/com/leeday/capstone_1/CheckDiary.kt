package com.leeday.capstone_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView

class CheckDiary : Fragment() {
    private lateinit var diaryEntry: DiaryData

    companion object {
        private const val ARG_DIARY = "argDiary"

        fun newInstance(diary: DiaryData): CheckDiary {
            val fragment = CheckDiary()
            val args = Bundle().apply {
                putSerializable(ARG_DIARY, diary)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.check_diary, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        diaryEntry = arguments?.getSerializable(ARG_DIARY) as DiaryData

        // Set diary content to views
        val subjectTextView = view.findViewById<TextView>(R.id.subjectTextView)
        val contentTextView = view.findViewById<TextView>(R.id.contentTextView)
        val emotionTextView = view.findViewById<TextView>(R.id.emotionTextView)

        subjectTextView.text = diaryEntry.subject
        contentTextView.text = diaryEntry.content
        emotionTextView.text = diaryEntry.emotion

        // Random background setting
        val backgrounds = arrayOf(R.drawable.diary1, R.drawable.diary2, R.drawable.diary4, R.drawable.diary5, R.drawable.diary6) // 원하는 배경 리소스를 추가하십시오
        val randomBackground = backgrounds[(0 until backgrounds.size).random()]

        // Find the LinearLayout by its ID and set its background
        val layout = view.findViewById<LinearLayout>(R.id.check_diary)
        layout.setBackgroundResource(randomBackground)
    }
}