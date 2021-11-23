package com.skillbox.shared_model.contact

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Contact(
    override val id: Long,
    override val name: String,
    override val numbers: String,
    override val avatar: Bitmap?
) : BaseContact(id, name, numbers, avatar),
    Parcelable
