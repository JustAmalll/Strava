package com.skillbox.core.snackbar

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.skillbox.core.R
import com.skillbox.core.extensions.getDimension
import com.skillbox.core.extensions.gone
import com.skillbox.core.extensions.show

class CustomSnackBar(
    parent: ViewGroup,
    content: CustomSnackBarView
) : BaseTransientBottomBar<CustomSnackBar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.TOP
        params.topMargin = view.context.getDimension(100F)
        getView().layoutParams = params
    }

    companion object {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun make(
            viewGroup: ViewGroup,
            isCache: Boolean,
            text: String,
            isError: Boolean = false,
            retry: () -> Unit
        ): CustomSnackBar {
            val customView = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.layout_custom_snackbar,
                viewGroup,
                false
            ) as CustomSnackBarView

            val tvMessage = customView.findViewById<TextView>(R.id.tvMessage)
            val ivState = customView.findViewById<ImageView>(R.id.ivState)
            val tvRetry = customView.findViewById<TextView>(R.id.tvRetry)
            val ivClose = customView.findViewById<ImageView>(R.id.ivClose)

            if (isCache) {
                tvMessage.setTextColor(
                    ContextCompat.getColor(
                        customView.context,
                        android.R.color.white
                    )
                )
                customView.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        customView.context,
                        R.drawable.rounded_cache
                    )
                )
                ivState.setBackgroundDrawable(customView.context.getDrawable(R.drawable.ic_notification))
            } else {
                tvMessage.setTextColor(
                    ContextCompat.getColor(
                        customView.context,
                        android.R.color.white
                    )
                )
                customView.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        customView.context,
                        R.drawable.rounded_error
                    )
                )
                ivState.setBackgroundDrawable(customView.context.getDrawable(R.drawable.ic_error))
            }
            tvMessage.text = text

            if (isError) tvRetry.show() else tvRetry.gone()

            tvRetry.setOnClickListener { retry.invoke() }
            ivClose.setOnClickListener {}

            return CustomSnackBar(viewGroup, customView)
        }
    }
}