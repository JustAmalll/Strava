package com.skillbox.strava.ui.fragment.onboarding

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.shared_model.network.BoardingModel
import com.skillbox.strava.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardingViewModel @Inject constructor() : BaseViewModel() {

    private var screens: MutableList<BoardingModel> = emptyList<BoardingModel>().toMutableList()

    fun getScreen(isDarkTheme: Boolean): List<BoardingModel> {
        screens.clear()
        screens.addAll(
            listOf(
                BoardingModel(
                    R.string.boarding_card_title_one,
                    R.string.boarding_card_desc_one,
                    R.drawable.ic_welcome_ascent
                ),
                BoardingModel(
                    R.string.boarding_card_title_two,
                    R.string.boarding_card_desc_two,
                    if (isDarkTheme) R.drawable.ic_friends_dark else R.drawable.ic_friends
                ),
                BoardingModel(
                    R.string.boarding_card_title_tree,
                    R.string.boarding_card_desc_tree,
                    if (isDarkTheme) R.drawable.ic_activities_dark else R.drawable.ic_activities
                )
            )
        )
        return screens
    }
}