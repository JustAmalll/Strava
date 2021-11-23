package com.skillbox.core_cotentProvider.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import androidx.annotation.DrawableRes
import com.skillbox.shared_model.contact.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject


class ContactRepositoryImpl @Inject constructor() : ContactRepository {

    override suspend fun getAllContacts(
        context: Context,
        @DrawableRes default: Int
    ): List<Contact> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )?.use {
            getContactsFromCursor(it, context, default)
        }.orEmpty()
    }

    private fun getContactsFromCursor(
        cursor: Cursor,
        context: Context,
        @DrawableRes default: Int
    ): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list: MutableList<Contact> = mutableListOf()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)

            list.add(
                Contact(
                    id = id,
                    name = name,
                    numbers = getPhonesForContact(id, context),
                    avatar = getPhotoForContact(id, context, default)
                )
            )
        } while (cursor.moveToNext())
        return list
    }

    private fun getPhonesForContact(id: Long, context: Context): String {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(id.toString()),
            null
        )?.use {
            getPhonesFromCursor(it).firstOrNull() ?: ""
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list: MutableList<String> = mutableListOf()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number = cursor.getString(numberIndex)
            list.add(number)
        } while (cursor.moveToNext())
        return list
    }

    private fun getPhotoForContact(
        contactId: Long,
        context: Context,
        @DrawableRes default: Int
    ): Bitmap? {
        var photo = BitmapFactory.decodeResource(context.resources, default)
        val inputStream: InputStream? = ContactsContract.Contacts.openContactPhotoInputStream(
            context.contentResolver,
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        )
        inputStream?.let {
            photo = BitmapFactory.decodeStream(inputStream)
        }
        inputStream?.close()
        return photo
    }
}