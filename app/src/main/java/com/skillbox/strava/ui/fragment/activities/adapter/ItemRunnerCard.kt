package com.skillbox.strava.ui.fragment.activities.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.skillbox.core.extensions.getAbbreviatedFromDateTime
import com.skillbox.core.extensions.roundTo2DecimalPlaces
import com.skillbox.core_db.pref.Pref
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.strava.R

@SuppressLint("SetTextI18n")
fun itemRunnerCard(pref: Pref) =
    adapterDelegateLayoutContainer<СreateActivity, Any>(R.layout.item_runner) {

        bind {
            val profileImageUrl = pref.photoprofile
            val nameProfile = pref.nameProfile

            val image = containerView.findViewById<ImageView>(R.id.runner_ivPhoto)
            val timeTitle = containerView.findViewById<TextView>(R.id.runner_tvTimeTitle)
            val name = containerView.findViewById<TextView>(R.id.runner_tvName)
            val title = containerView.findViewById<TextView>(R.id.runner_tvTitle)
            val distance = containerView.findViewById<TextView>(R.id.runner_tvDistanceValue)
            val timeValue = containerView.findViewById<TextView>(R.id.runner_tvTimeValue)
            val elevation = containerView.findViewById<TextView>(R.id.runner_tvElevationValue)

            if (profileImageUrl.isNotBlank()) {
                Glide.with(containerView.context)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_placeholder_contact)
                    .error(R.drawable.ic_error_contact)
                    .transform(CircleCrop())
                    .into(image)
            }

            if (item.start_date.isNotEmpty()) {
                val formatDate = getAbbreviatedFromDateTime(item.start_date)
                timeTitle.text = formatDate
            }
            if (nameProfile.isNotEmpty()) name.text = nameProfile

            title.text = item.name
            distance.text = item.distance?.let { "${(it / 1000).roundTo2DecimalPlaces()} km" }

            val hour = (item.elapsed_time / 60)
            val minutes = item.elapsed_time - (hour * 60)
            timeValue.text = "$hour h $minutes m"
            elevation.text = "${item.total_elevation_gain} m"
        }
    }