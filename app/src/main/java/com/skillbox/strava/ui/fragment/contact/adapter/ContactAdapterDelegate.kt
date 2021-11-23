package com.skillbox.strava.ui.fragment.contact.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.skillbox.core.extensions.inflate
import com.skillbox.shared_model.contact.Contact
import com.skillbox.strava.R
import kotlinx.android.extensions.LayoutContainer

class ContactAdapterDelegate(
    private val onItemClick: (Contact: Contact) -> Unit
) : AbsListItemAdapterDelegate<Contact, Contact, ContactAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        Holder(parent.inflate(R.layout.item_contact), onItemClick)

    override fun isForViewType(
        item: Contact,
        items: MutableList<Contact>,
        position: Int
    ) = true

    override fun onBindViewHolder(item: Contact, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        private val onItemClick: (contact: Contact) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val name = itemView.findViewById<TextView>(R.id.item_textViewName)
        private val number = itemView.findViewById<TextView>(R.id.item_textViewNumber)
        private val image = itemView.findViewById<ImageView>(R.id.item_image)

        fun bind(item: Contact) {
            containerView.setOnClickListener {
                onItemClick(item)
            }

            name.text = item.name
            number.text = item.numbers

            Glide.with(itemView)
                .load(item.avatar ?: R.drawable.ic_error_contact)
                .placeholder(R.drawable.ic_placeholder_contact)
                .error(R.drawable.ic_error_contact)
                .transform(CircleCrop())
                .into(image)
        }
    }
}