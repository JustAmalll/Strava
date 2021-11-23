package com.skillbox.strava.ui.fragment.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skillbox.shared_model.network.BoardingModel

class OnboardingAdapter(
    private val screens: List<BoardingModel>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment {
        val screen: BoardingModel = screens[position]
        return OnboardingFragment.newInstance(
            titleRes = screen.title,
            descRes = screen.description,
            drawableRes = screen.drawable
        )
    }
}
