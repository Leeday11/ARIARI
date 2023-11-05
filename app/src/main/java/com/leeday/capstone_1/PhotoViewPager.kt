// PhotoViewPager.kt
package com.leeday.capstone_1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PhotoViewPager(private val photos: List<Pair<String, String>>, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = photos.size

    override fun getItem(position: Int): Fragment {
        val (createDate, imagePath) = photos[position]
        // CheckPhotoFragment의 newInstance 메소드를 호출하여 Fragment를 생성합니다.
        return CheckPhoto.newInstance(createDate, imagePath)
    }
}
