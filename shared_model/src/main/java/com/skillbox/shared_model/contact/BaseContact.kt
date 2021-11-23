package com.skillbox.shared_model.contact

import android.graphics.Bitmap

abstract class BaseContact(
    open val id: Long,
    open val name: String,
    open val numbers: String,
    open val avatar: Bitmap?
)