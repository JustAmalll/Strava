package com.skillbox.strava.ui.fragment.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.skillbox.core.extensions.checkDarkTheme
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.utils.ZoomOutPageTransformer
import com.skillbox.core_db.pref.Pref
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentBoardingBinding
import com.skillbox.strava.ui.fragment.onboarding.adapter.OnboardingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingFragment :
    ViewBindingFragment<FragmentBoardingBinding>(FragmentBoardingBinding::inflate) {

    override val screenViewModel by viewModels<BoardingViewModel>()
    private var positionSelected = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        bindViewPager()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun bind() {
        val imageDrawable = if (requireContext().checkDarkTheme())
            requireContext().getDrawable(R.drawable.ic_logo_dark)
        else
            requireContext().getDrawable(R.drawable.ic_logo_light)

        binding.boardingImageLogo.setBackgroundDrawable(imageDrawable)
    }

    private fun bindViewPager() {
        val screens = screenViewModel.getScreen(requireContext().checkDarkTheme())
        val adapter = OnboardingAdapter(screens, requireActivity())
        binding.boardingCard.adapter = adapter
        binding.boardingCard.setPageTransformer(ZoomOutPageTransformer())
        binding.boardingCard.offscreenPageLimit = 1
        binding.boardingDotsIndicator.setViewPager2(binding.boardingCard)

        binding.boardingSkip.setOnClickListener {
            if (positionSelected < 2)
                binding.boardingCard.setCurrentItem(positionSelected + 1, true)
            else navigateAuth()
        }
        binding.boardingCard.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                positionSelected = position
                binding.boardingSkip.text =
                    if (position < 2) getString(R.string.boarding_button_skip)
                    else getString(R.string.boarding_button_okay)
            }
        })
    }

    private fun navigateAuth() {
        Pref(requireContext(), requireActivity().application).isBoarding = true
        findNavController()
            .navigate(R.id.action_boardingFragment_to_authFragment)
    }
}