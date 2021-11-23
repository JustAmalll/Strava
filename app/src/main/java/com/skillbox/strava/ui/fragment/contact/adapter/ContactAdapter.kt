package com.skillbox.strava.ui.fragment.contact.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skillbox.shared_model.contact.Contact

class ContactAdapter(onItemClick: (position: Contact) -> Unit) :
    AsyncListDifferDelegationAdapter<Contact>(
        UserDiffUtilCallback()
    ) {

    init {
        delegatesManager.addDelegate(
            ContactAdapterDelegate(onItemClick)
        )
    }

    class UserDiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean = true
    }
}