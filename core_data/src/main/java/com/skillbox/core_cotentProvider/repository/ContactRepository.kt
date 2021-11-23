package com.skillbox.core_cotentProvider.repository

import android.content.Context
import androidx.annotation.DrawableRes
import com.skillbox.shared_model.contact.Contact

interface ContactRepository {
    suspend fun getAllContacts(context: Context, @DrawableRes default: Int): List<Contact>
}