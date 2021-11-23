package com.skillbox.strava.ui.fragment.onboarding.adapter

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.skillbox.core.extensions.withArguments
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.strava.databinding.ItemCardBinding

class OnboardingFragment : ViewBindingFragment<ItemCardBinding>(ItemCardBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.cardImage.setImageResource(requireArguments().getInt(KEY_IMAGE))
        binding.cardTitle.setText(requireArguments().getInt(KEY_TITLE))
        binding.cardDesc.setText(requireArguments().getInt(KEY_DESC))
    }

    companion object {

        private const val KEY_TITLE = "title"
        private const val KEY_DESC = "desc"
        private const val KEY_IMAGE = "image"

        fun newInstance(
            @StringRes titleRes: Int,
            @StringRes descRes: Int,
            @DrawableRes drawableRes: Int
        ): OnboardingFragment {
            return OnboardingFragment().withArguments {
                putInt(KEY_TITLE, titleRes)
                putInt(KEY_DESC, descRes)
                putInt(KEY_IMAGE, drawableRes)
            }
        }
    }
}
