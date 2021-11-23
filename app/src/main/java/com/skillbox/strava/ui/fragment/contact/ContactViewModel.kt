package com.skillbox.strava.ui.fragment.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_cotentProvider.repository.ContactRepository
import com.skillbox.shared_model.contact.Contact
import com.skillbox.strava.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : BaseViewModel() {

    val contactObserver = SingleLiveEvent<List<Contact>>()
    val loadDataObserver = SingleLiveEvent<Boolean>()

    fun getContacts(context: Context) {
        loadDataObserver.postValue(true)
        launchIO {
            try {
                val contact = repository.getAllContacts(context, R.drawable.ic_error_contact)
                contact.let {
                    contactObserver.postValue(it)
                }
            } catch (e: Exception) {
                contactObserver.postValue(arrayListOf())
            } finally {
                loadDataObserver.postValue(false)
            }
        }
    }

    fun getIntentContact(number: String, userId: Long) =
        Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number")).apply {
            putExtra(SMS_BODY, "Я уже в Strava: https://strava/athletes/?userId=${userId}")
        }

    companion object {
        const val SMS_BODY = "sms_body"
    }
}