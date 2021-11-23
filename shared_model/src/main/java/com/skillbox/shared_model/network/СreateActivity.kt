package com.skillbox.shared_model.network

data class СreateActivity(
    val id: Long,
    val name: String,
    val type: String,
    val start_date: String,
    val elapsed_time: Int,
    val description: String? = "",
    val distance: Float? = 0F,
    val total_elevation_gain: Int? = 0
)
