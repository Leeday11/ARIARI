package com.leeday.capstone_1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// DiaryPagerAdapter는 ViewPager의 어댑터로서 동작
// 별도의 XML파일은 필요없음

class DiaryPagerAdapter(private val diaries: List<DiaryData>, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = diaries.size

    override fun getItem(position: Int): Fragment {
        return CheckDiary.newInstance(diaries[position])
    }
}
