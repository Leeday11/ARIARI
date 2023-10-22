package com.leeday.capstone_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
}
