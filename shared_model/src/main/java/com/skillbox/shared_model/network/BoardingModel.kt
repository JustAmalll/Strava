package com.skillbox.shared_model.network

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BoardingModel(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val drawable: Int
)
