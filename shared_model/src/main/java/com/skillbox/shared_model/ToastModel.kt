package com.skillbox.shared_model

data class ToastModel(
    val text: String,
    val isLocal: Boolean,
    val isError: Boolean = false
)
