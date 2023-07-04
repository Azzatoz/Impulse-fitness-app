package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.workoutsList.ExistingWorkouts
import com.example.mydiplom_try2.workoutsList.UserWorkoutsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ExerciseSelectionMenu : Fragment(R.layout.fragment_selection_menu) {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)


        soundManager = SoundManager

        setupViewPager()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Существующие тренировки"
                1 -> tab.text = "Мои тренировки"
            }
        }.attach()
    }

    private fun setupViewPager() {
        viewPager.adapter = ViewPagerAdapter(requireActivity())
    }

    private inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val fragmentList = listOf(ExistingWorkouts(), UserWorkoutsFragment())

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }
}
